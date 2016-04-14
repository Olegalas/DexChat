package ua.dexchat.server.web_sockets;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import ua.dexchat.model.WebSocketMessage;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dexter on 14.04.16.
 */
public class FileServerSocket extends WebSocketServer {

    private static final Logger LOGGER = Logger.getLogger(ServerWebSocket.class);

    private Map<String, WebSocket> socketsWaiting = new HashMap<>();

    public FileServerSocket(int port, Map<String, WebSocket> socketsWaiting) throws UnknownHostException {
        super(new InetSocketAddress(port));
        this.socketsWaiting = socketsWaiting;
    }

    public FileServerSocket(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        WebSocketMessage message = new WebSocketMessage("connection complete", WebSocketMessage.MessageType.TEXT);
        LOGGER.info("***" + webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        LOGGER.info("***" + webSocket + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        // this method won't be using
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        LOGGER.info("***file was receive");
        WebSocket receiver = socketsWaiting.get(conn.getRemoteSocketAddress().toString());
        receiver.send(message);
        LOGGER.info("***file was sent");
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        LOGGER.error("***"+e.getMessage());
        if(webSocket != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
            LOGGER.error("conn == null");
        }
    }
}
