import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by dexter on 02.04.16.
 */
public class ClientLogin extends Thread {

    private static boolean interrupt = false;

    private final Socket clientSocket;
    private final static Logger LOGGER = Logger.getLogger(Server.class);
    private final ClientInfoSocket clientInfo;

    ClientLogin(Socket client, ClientInfoSocket info){
        this.clientSocket = client;
        clientInfo = info;
    }

    @Override
    public void run() {

        LOGGER.debug("ClientLogin " + clientInfo + " thread was run");

        while (!interrupt || !isInterrupted()){

            try {
                LOGGER.debug(clientInfo +" - Wait for Login object from client");

                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                Login loginFromClient = (Login) ois.readObject();

                LOGGER.debug(clientInfo +" - Gut " + loginFromClient + " from client");

                if(loginFromClient == null || loginFromClient.login.isEmpty() || loginFromClient.pass.isEmpty()){
                    continue;
                }

                if(loginFromClient.name.isEmpty()){
                    Client client = login(loginFromClient);
                    if(client != null){
                        new Conversation(clientSocket, clientInfo, client).run();
                        interrupt();
                        break;
                    }
                } else {
                    registration(loginFromClient);
                }


            } catch (IOException e) {
                LOGGER.error("IOException from input stream in "+ clientInfo +" - " + e.getMessage());
                break;
            } catch (ClassNotFoundException e){
                LOGGER.error("ClassNotFoundException from input stream in "+ clientInfo +" - " + e.getMessage());
            }
        }

        LOGGER.debug("ClientLogin " + clientInfo + " thread was correctly killed");
    }

    private Client login(Login login){

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dexunit");
        EntityManager manager = entityManagerFactory.createEntityManager();

        manager.getTransaction().begin();

        Query query =
                manager.createQuery("SELECT c FROM Client c WHERE c.login = :login");

        LOGGER.debug(clientInfo +" - Query was created for login");
        Client fromResult = (Client) query.setParameter("login", login.login).getSingleResult();
        LOGGER.debug("Client was founded");
        LOGGER.debug(fromResult);

        manager.getTransaction().commit();

        return login.pass.equals(fromResult.getPass()) ? fromResult : null;
    }

    private boolean registration(Login login) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dexunit");
        EntityManager manager = entityManagerFactory.createEntityManager();

        Client client = new Client(login);

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

}
