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

    ClientConnect(Socket client, ClientInfoSocket info){
        this.client = client;
        clientInfo = info;
    }

    // In this thread server do validation and registration for client-side
    // for choose registration client-side must send "1"
    // fot choose validation client-side must send "2"
    // then server takes login and pass and check it out
    // finally if client passed validation - server give to client new thread where they will work
    @Override
    public void run() {

        LOGGER.debug("ClientConnect " + clientInfo + " thread was run");
        String choose = "";

        while (!isInterrupted()){

            try {
                LOGGER.debug(clientInfo +" - Wait for choose from client");

                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                choose = br.readLine();
                choose = choose.substring(0,1);

                LOGGER.debug(clientInfo +" - Gut " + choose + " from client");
            } catch (IOException e) {
                LOGGER.error("IOException from input stream in "+ clientInfo +" - " + e.getMessage());
                break;
            }

            switch (choose){

                case REGISTRATION:
                    LOGGER.debug(clientInfo +" - REGISTRATION");
                    registration();
                    LOGGER.debug(clientInfo +" - REGISTRATION completed");
                    break;
                case VALIDATION:
                    LOGGER.debug(clientInfo +" - VALIDATION");
                    if(validation()){
                        /* run new thread and interrupted this thread*/
                        LOGGER.info("Client passed validation " + clientInfo);
                    }
                    break;

            }
        }

        LOGGER.debug("ClientConnect " + clientInfo + " thread was killed");
    }

    private boolean validation() {

        Client client = null;
        try {
            client = getLoginAndPassFromClient();
        } catch (IOException e) {
            LOGGER.error("IOException from input stream in "+ clientInfo +" - " + e.getMessage());
            LOGGER.error("getLoginAndPassFromClient returned null");
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

    //// TODO: 30.03.16
    private void registration() {
        try {
            getLoginAndPassFromClient();
        } catch (IOException e) {
            LOGGER.error("IOException from input stream in "+ clientInfo +" - " + e.getMessage());
            LOGGER.error("getLoginAndPassFromClient returned null");
        }
    }

    private Client getLoginAndPassFromClient() throws IOException{

            Login login = new Login();
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

            LOGGER.info("ClientConnect " + clientInfo + " wait for login");
            login.name = br.readLine();
            LOGGER.info("ClientConnect " + clientInfo + " wait for pass");
            login.pass = br.readLine();

            br.close();
            return new Client(login);
    }


}
