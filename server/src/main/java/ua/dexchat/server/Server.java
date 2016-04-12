package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocketImpl;
import ua.dexchat.model.ClientInfoSocket;
import ua.dexchat.server.web_sockets.ServerWebSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dexter on 28.03.16.
 */
public final class Server {

    private final static Logger LOGGER = Logger.getLogger(Server.class);
    private final static Server instance = new Server();

    private Server(){
    }

    public static Server newInstance(){
        return instance;
    }

    public void run(){
        try{

            int port = 8887;

            ServerWebSocket s = new ServerWebSocket( port );
            s.start();

        } catch (IOException e){
            LOGGER.error("***server caught IOException - " + e.getMessage());
        }
    }
}
