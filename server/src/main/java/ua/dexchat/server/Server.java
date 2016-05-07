package ua.dexchat.server;

import org.apache.log4j.Logger;
import ua.dexchat.server.web_sockets.FileServerSocket;
import ua.dexchat.server.web_sockets.ServerWebSocket;

import java.io.IOException;

/**
 * Created by dexter on 28.03.16.
 */
public final class Server {

    private static final int MAIN_PORT  = 8887;
    private static final int PORT_FOR_RESENT_FILE = 8888;

    private final static Logger LOGGER = Logger.getLogger(Server.class);
    private final static Server instance = new Server();

    private Server(){
    }

    public static Server newInstance(){
        return instance;
    }

    public void run(){
        try{

            ServerWebSocket server = new ServerWebSocket(MAIN_PORT);
            FileServerSocket fileServer = new FileServerSocket(PORT_FOR_RESENT_FILE, server.getSocketsWaiting());
            server.start();
            fileServer.start();
            LOGGER.info("***Server was run");

        } catch (IOException e){
            LOGGER.error("***server caught IOException - " + e.getMessage());
        }
    }
}
