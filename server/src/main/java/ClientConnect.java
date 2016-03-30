import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

/**
 * Created by dexter on 28.03.16.
 */
public class ClientConnect extends Thread {

    private final static String REGISTRATION = "1";
    private final static String VALIDATION = "2";

    private final Socket client;
    private final static Logger LOGGER = Logger.getLogger(Server.class);
    private final ClientInfoSocket clientInfo;
    private BufferedReader br;

    ClientConnect(Socket client, ClientInfoSocket info){
        this.client = client;
        clientInfo = info;
    }

    // In this thread server do validation and registration for client-side
    // for choose registration client-side must send "1"
    // fot choose validation client-side must send "2"
    // then server takes login and pass and check it out
    // finally if client passed validation - server give to client new thread where they will work
    // when the client-side send <@exit> combination this thread will die
    @Override
    public void run() {

        LOGGER.debug("ClientConnect " + clientInfo + " thread was run");
        String choose = "";

        while (!isInterrupted()){

            try {
                LOGGER.debug(clientInfo +" - Wait for choose from client");

                br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                choose = br.readLine();
                checkExit(choose);
                choose = choose.substring(0,1);

                LOGGER.debug(clientInfo +" - Gut " + choose + " from client");
            } catch (IOException e) {
                LOGGER.error("IOException from input stream in "+ clientInfo +" - " + e.getMessage());
                break;
            } catch (InterruptedException e){
                LOGGER.fatal("Client close client-side application");
                break;
            }

            switch (choose){

                case REGISTRATION:
                    LOGGER.debug(clientInfo +" - REGISTRATION");
                    while(!isInterrupted()){
                        try {
                            if(registration()){
                                LOGGER.debug(clientInfo +" - REGISTRATION completed");
                                break;
                            }
                        } catch (InterruptedException e) {
                            LOGGER.info(clientInfo +" - quit from registration");
                            break;
                        }
                    }
                    break;
                case VALIDATION:
                    LOGGER.debug(clientInfo +" - VALIDATION");
                    try {
                        if(validation()){
                            /* run new thread and interrupted this thread*/
                            LOGGER.info("Client passed validation " + clientInfo);
                        }
                    } catch (InterruptedException e) {
                        LOGGER.fatal("Client close client-side application");
                    }
                    break;

            }
        }

        LOGGER.debug("ClientConnect " + clientInfo + " thread was killed");
    }

    private boolean validation() throws InterruptedException {

        Client client = null;
        try {
            client = getLoginAndPassFromClient();
        } catch (IOException e) {
            LOGGER.error("IOException from input stream in "+ clientInfo +" - " + e.getMessage());
            LOGGER.error("getLoginAndPassFromClient returned false");
            return false;
        }

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dexunit");
        EntityManager manager = entityManagerFactory.createEntityManager();

        manager.getTransaction().begin();

        Query query =
                manager.createQuery("SELECT c FROM Client c WHERE c.login = :login");

        LOGGER.debug(clientInfo +" - Query was created for validation");
        Client fromResult = (Client) query.setParameter("login", client.getLogin()).getSingleResult();
        LOGGER.debug("**********Client was created");
        LOGGER.debug(client);

        manager.getTransaction().commit();

        return client.getPass().equals(fromResult.getPass());
    }

    private boolean registration() throws InterruptedException{
        Client client = null;
        try {

            client = getLoginAndPassFromClient();
            String name = "";
            LOGGER.info("ClientConnect " + clientInfo + " wait for user name");
            name = br.readLine();
            checkExit(name);

            client.setName(name);

        } catch (IOException e) {
            LOGGER.error("IOException from input stream in "+ clientInfo +" - " + e.getMessage());
            LOGGER.error("getLoginAndPassFromClient returned false");
            return false;
        }

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dexunit");
        EntityManager manager = entityManagerFactory.createEntityManager();
        try{
            manager.getTransaction().begin();
            manager.persist(client);
            manager.getTransaction().commit();

        }catch (Exception e){
            LOGGER.error("This login has already used");
            return false;
        }

        return true;
    }

    private Client getLoginAndPassFromClient() throws IOException, InterruptedException {

        Login login = new Login();

        LOGGER.info("ClientConnect " + clientInfo + " wait for login");
        login.name = br.readLine();
        checkExit(login.name);
        LOGGER.info("ClientConnect " + clientInfo + " wait for pass");
        login.pass = br.readLine();
        checkExit(login.pass);

        return new Client(login);
    }

    private void checkExit(String exitCommand) throws InterruptedException {
        if("@exit".equals(exitCommand)){
            throw new InterruptedException("user want exit from registration");
        }
    }


}
