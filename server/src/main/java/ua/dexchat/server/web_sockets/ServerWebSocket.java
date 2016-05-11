package ua.dexchat.server.web_sockets;

import com.google.gson.internal.LinkedTreeMap;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import ua.dexchat.model.*;
import ua.dexchat.server.*;
import ua.dexchat.server.utils.JsonUtils;
import ua.dexchat.server.utils.WebSocketUtils;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by dexter on 11.04.16.
 */
public class ServerWebSocket extends WebSocketServer {

    private static final Logger LOGGER = Logger.getLogger(ServerWebSocket.class);
    private Map<Integer, WebSocket> sockets = new ConcurrentHashMap<>(1000);
    private Map<String, WebSocket> socketsWaiting = new ConcurrentHashMap<>(1000);
    private Map<WebSocket, String> ipAddresses = new ConcurrentHashMap<>(1000);
    private Map<WebSocket, Thread> threads = new ConcurrentHashMap<>(1000);

    public ServerWebSocket(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public ServerWebSocket(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        LOGGER.info("***" + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " : entered the room!");
        WebSocketUtils.sendTextMessageToClient("connection complete", conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        LOGGER.info("***" + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " : has left the room!");
        Thread messageServiceThread = threads.get(conn);
        if(messageServiceThread != null){
            messageServiceThread.interrupt();
            LOGGER.info("***Server sent interrupt command to messageServiceThread");
        }
    }

    @Override
    public void onMessage(WebSocket conn, String messageString) {

        LOGGER.info("***" + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " : " + messageString);
        WebSocketMessage webSocketMessage = JsonUtils.parseWebSocketMessage(messageString);
        LinkedTreeMap map = (LinkedTreeMap) webSocketMessage.getMessage();

        switch (webSocketMessage.getType()) {

            case LOGIN: {
                LOGGER.info("***LOGIN case");

                Login login = new Login(null, (String) map.get("pass"), (String) map.get("login"), (String) map.get("ip"));
                new LoginClientThread(conn, login, sockets, ipAddresses, threads).start();

                break;
            }
            case REGISTRATION: {

                LOGGER.info("***REGISTRATION case");
                Login login = new Login((String) map.get("name"), (String) map.get("pass"), (String) map.get("login"), (String) map.get("ip"), (String) map.get("email"));
                new RegistrationThread(conn, login).start();
                break;
            }
            case FILE: {

                LOGGER.info("***FILE case");
                FileMessage fileMessage = new FileMessage((String) map.get("fileName"), (String) map.get("fileType"), (String) map.get("idSender"), (String) map.get("idReceiver"));
                new NotifyReceiverFileSendThread(conn, fileMessage, sockets, socketsWaiting).start();
                break;
            }
            case CONFIRMATION: {

                LOGGER.info("***CONFIRMATION case");
                Confirmation confirmation = new Confirmation((String) map.get("confirm"), (String) map.get("idSender"));
                new ReceiverConfirmSendFileThread(conn, sockets, confirmation, socketsWaiting).start();

                break;
            }
            case MESSAGE: {

                LOGGER.info("***MESSAGE case");
                Message message = new Message((String) map.get("message"), (String) map.get("idSender"), (String) map.get("idReceiver"), (String) map.get("date"));
                new SendMessageThread(message).start();
                break;
            }
            case FRIEND: {

                LOGGER.debug("***FRIEND case");
                ClientDTO clientDTO = JsonUtils.getClientDTO(messageString);
                new FriendServiceThread(clientDTO, conn).start();

                break;
            }
            case EXIT: {

                LOGGER.info("***EXIT case");
                break;
            }
            case TEXT: {

                LOGGER.info("***TEXT case");
                break;
            }
            case EMAIL: {

                LOGGER.info("***EMAIL case");
                Login login = new Login(null, null, (String) map.get("login"), (String) map.get("ip"), (String) map.get("email"));
                new SendEmailThread(login.login, login.email, conn).start();
                break;
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        LOGGER.error("***OnError method...", ex);
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
