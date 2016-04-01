import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dexter on 28.03.16.
 */
public final class Server {

    private final static Server instance = new Server();
    private final static Logger LOGGER = Logger.getLogger(Server.class);
    private ServerSocket serverSocket;

    private Server(){}

    public static Server newInstance(){
        return instance;
    }

    public void run(){


        init(); // in this method I check out my data base... It's not correct, but whatever

        try{

            serverSocket = new ServerSocket(8080);
            LOGGER.info("server was run");
            while(true){

                Socket client = serverSocket.accept();

                ClientInfoSocket info = new ClientInfoSocket();
                info.ip = client.getInetAddress().getCanonicalHostName();
                info.port = client.getPort();

                LOGGER.info("new client was accepted");
                LOGGER.debug("Internet address new client - " + info);

                new ClientConnect(client, info).run();

            }
        } catch (ThreadDeath death){
            LOGGER.fatal("server was stopped by thread death exception");
        } catch (IOException e){
            LOGGER.error("server caught IOException - " + e.getMessage());
        }
    }


    // in this method I check out my data base... It's not correct, but whatever
    private void init(){

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dexunit");
        EntityManager manager = entityManagerFactory.createEntityManager();

        LOGGER.info("****************manager was created");

        Client client = new Client("client", "client", "client");

        MessageBuffer messageBuffer = new MessageBuffer();
        Message testMessage = new Message("testMessage", client.getId(), client.getId(), new Date(), messageBuffer);

        messageBuffer.getMessages().add(testMessage);
        messageBuffer.setIdOwner(client);
        messageBuffer.setIdSender(client.getId());

        client.getBuffers().add(messageBuffer);

        try{

            manager.getTransaction().begin();
            manager.persist(client);
            manager.getTransaction().commit();

        }catch (Exception e){
            LOGGER.info("****************test client has already created before");
        }


        manager.close();

        manager = entityManagerFactory.createEntityManager();

        try{

            manager.getTransaction().begin();
            Client clientFromDB = manager.find(Client.class, 1);
            manager.getTransaction().commit();
            manager.close();

            LOGGER.debug("******************* test client : " + clientFromDB);
            LOGGER.debug("******************* test message : " + clientFromDB.getBuffers().get(0).getMessages().get(0).getMessage());

        }catch (Exception e){
            LOGGER.info("*************** error ");
        }




    }

}
