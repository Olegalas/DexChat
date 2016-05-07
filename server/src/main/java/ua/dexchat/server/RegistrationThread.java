package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.springframework.context.ApplicationContext;
import ua.dexchat.model.Login;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;
import ua.dexchat.server.utils.WebSocketUtils;

/**
 * Created by dexter on 12.04.16.
 */
public class RegistrationThread extends Thread{

    private static final Logger LOGGER = Logger.getLogger(RegistrationThread.class);
    private final WebSocket clientSocket;
    private final Login login;

    public RegistrationThread(WebSocket socket, Login login){
        this.clientSocket = socket;
        this.login = login;
    }

    @Override
    public void run(){
        ApplicationContext context = GetSpringContext.getContext();
        ClientService service = context.getBean(ClientService.class);

        int idClient = service.saveClient(login);

        if(idClient == -1){
            WebSocketUtils.sendTextMessageToClient("Login or email has already used", clientSocket);
            LOGGER.info("***Login or email has already used");
        } else {
            WebSocketUtils.sendTextMessageToClient("Registration complete", clientSocket);
            LOGGER.info("***Registration complete");
        }
    }
}
