package ua.dexchat.server.web_sockets;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import ua.dexchat.model.*;
import ua.dexchat.server.ConfirmFileResendThread;
import ua.dexchat.server.LoginClientThread;
import ua.dexchat.server.RegistrationThread;
import ua.dexchat.server.SendMessageThread;
import ua.dexchat.server.utils.JsonUtils;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by dexter on 11.04.16.
 */
public class ServerWebSocket extends WebSocketServer {

    private static final Logger LOGGER = Logger.getLogger(ServerWebSocket.class);
    private Map<Integer, WebSocket> sockets = new HashMap<>();
    private Map<Integer, AtomicBoolean> confirms = new HashMap<>();
    private Map<String, WebSocket> socketsWaiting = new HashMap<>();

    public ServerWebSocket(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public ServerWebSocket(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        WebSocketMessage message = new WebSocketMessage("connection complete", WebSocketMessage.MessageType.TEXT);
        LOGGER.info("***" + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
        conn.send(JsonUtils.transformObjectInJson(message));

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        LOGGER.info("***" + conn + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket conn, String messageString) {
        LOGGER.info("***" + conn + ": " + messageString);
        WebSocketMessage webSocketMessage = JsonUtils.parseWebSocketMessage(messageString);
        switch (webSocketMessage.getType()) {

            case LOGIN: {

                Login login = JsonUtils.parseString(webSocketMessage.getMessage(), Login.class);
                new LoginClientThread(conn, login, sockets).start();
                break;
            }
            case REGISTRATION: {

                Login login = JsonUtils.parseString(webSocketMessage.getMessage(), Login.class);
                new RegistrationThread(conn, login).start();
                break;
            }
            case FILE: {

                FileMessage fileMessage = JsonUtils.parseString(webSocketMessage.getMessage(), FileMessage.class);
                new ConfirmFileResendThread(conn, fileMessage, sockets, confirms, socketsWaiting).start();
                break;
            }
            case CONFIRMATION: {

                Confirmation confirmation = JsonUtils.parseString(webSocketMessage.getMessage(), Confirmation.class);

                if (confirmation.confirm) {
                    // notify sender
                    AtomicBoolean confirm = confirms.get(confirmation.idSender);
                    confirm.set(true);
                } else {
                    // notify sender
                }

                break;
            }
            case MESSAGE: {

                Message message = JsonUtils.parseString(messageString, Message.class);
                new SendMessageThread(message).start();
                break;
            }
            case FRIEND: {

                break;
            }
            case EXIT: {

                break;
            }
            case TEXT: {

                break;
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        LOGGER.error("***" + ex.getMessage());
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
            LOGGER.error("conn == null");
        }
    }

    /**
     * Sends <var>text</var> to all currently connected WebSocket clients.
     *
     * @param text The String to send across the network.
     * @throws InterruptedException When socket related I/O errors occur.
     */
    public void sendToAll(String text) {
        Collection<WebSocket> con = connections();
        synchronized (con) {
            for (WebSocket c : con) {
                c.send(text);
            }
        }
    }

    public Map<String, WebSocket> getSocketsWaiting() {
        return socketsWaiting;
    }
}
