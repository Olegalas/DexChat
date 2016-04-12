package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.springframework.context.ApplicationContext;
import ua.dexchat.model.Client;
import ua.dexchat.model.Login;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;
import ua.dexchat.server.service.WebSocketUtils;

/**
 * Created by dexter on 12.04.16.
 */
public class Registration extends Thread{

    private static final Logger LOGGER = Logger.getLogger(LoginClient.class);
    private final WebSocket clientSocket;
    private final Login login;

    public Registration(WebSocket socket, Login login){
        this.clientSocket = socket;
        this.login = login;
    }

    @Override
    public void run(){

        ApplicationContext context = GetSpringContext.getContext();
        ClientService service = context.getBean(ClientService.class);

        int idClient = service.saveClient(login);

        if(idClient == -1){
            WebSocketUtils.sendTextMessageToClient("Incorrect pass or Login", clientSocket);
        } else {
            WebSocketUtils.sendTextMessageToClient("Registration complete", clientSocket);
        }
    }
}
