package ua.dexchat.server;

import org.apache.log4j.Logger;
import ua.dexchat.model.ClientInfoSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

        try{

            serverSocket = new ServerSocket(8080);
            LOGGER.info("***server was run");
            while(true){

                Socket client = serverSocket.accept();

                ClientInfoSocket info = new ClientInfoSocket(client);

                LOGGER.info("***new client was accepted");
                LOGGER.info("***Internet address new client - " + info);

                new ClientLogin(client, info).start();

            }
        } catch (ThreadDeath death){
            LOGGER.fatal("***server was stopped by thread death exception");
        } catch (IOException e){
            LOGGER.error("***server caught IOException - " + e.getMessage());
        }
    }

}
