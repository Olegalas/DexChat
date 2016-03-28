import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dexter on 28.03.16.
 */
public final class Server {

    private final static Logger LOGGER = Logger.getLogger(Server.class);
    private ServerSocket serverSocket;

    public void run(){

        init();

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

    private void init(){
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("myunit");

        EntityManager manager = entityManagerFactory.createEntityManager();
    }
}
