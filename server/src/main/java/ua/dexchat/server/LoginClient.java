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
public class LoginClient extends Thread {

    private static final Logger LOGGER = Logger.getLogger(LoginClient.class);
    private final WebSocket clientSocket;
    private final Login login;
    private final Map<Integer, WebSocket> sockets;

    public LoginClient(WebSocket socket, Login login, Map<Integer, WebSocket> sockets){
        this.clientSocket = socket;
        this.login = login;
        this.sockets = sockets;
    }

    @Override
    public void run(){

        ApplicationContext context = GetSpringContext.getContext();
        ClientService service = context.getBean(ClientService.class);

        Client client = service.findClient(login);
        if(client == null){
            WebSocketUtils.sendTextMessageToClient("Incorrect pass or Login", clientSocket);
        } else {
            WebSocketUtils.sendTextMessageToClient("Welcome to DexChat", clientSocket);
            sockets.put(client.getId(), clientSocket);
        }
    }
}
