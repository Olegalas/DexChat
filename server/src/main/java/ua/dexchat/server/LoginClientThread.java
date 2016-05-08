package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.springframework.context.ApplicationContext;
import ua.dexchat.model.Client;
import ua.dexchat.model.Login;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;
import ua.dexchat.server.utils.WebSocketUtils;

import java.util.Map;

/**
 * Created by dexter on 12.04.16.
 */
public class LoginClientThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(LoginClientThread.class);
    private final WebSocket clientSocket;
    private final Login login;
    private final Map<Integer, WebSocket> sockets;
    private final Map<WebSocket, String> ipAddresses;
    private final Map<WebSocket, Thread> threads;

    public LoginClientThread(WebSocket socket, Login login, Map<Integer, WebSocket> sockets, Map<WebSocket, String> ipAddresses, Map<WebSocket, Thread> threads){
        this.clientSocket = socket;
        this.login = login;
        this.sockets = sockets;
        this.ipAddresses = ipAddresses;
        this.threads = threads;
    }

    @Override
    public void run(){

        ApplicationContext context = GetSpringContext.getContext();
        ClientService service = context.getBean(ClientService.class);

        Client client = service.findClientByLoginObject(login);
        if(client == null){
            WebSocketUtils.sendTextMessageToClient("Incorrect pass or Login", clientSocket);
        } else {
            WebSocketUtils.sendTextMessageToClient("Welcome to DexChat", clientSocket);
            sockets.put(client.getId(), clientSocket);
            ipAddresses.put(clientSocket, login.id);
            Thread messageServiceThread = new MessageServiceThread(client, clientSocket);
            messageServiceThread.start();
            threads.put(clientSocket, messageServiceThread);
        }
    }
}
