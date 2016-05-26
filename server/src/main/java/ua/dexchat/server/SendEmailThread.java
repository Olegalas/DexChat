package ua.dexchat.server;

import org.java_websocket.WebSocket;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;

/**
 * Created by dexter on 09.05.16.
 */
public class SendEmailThread extends Thread {

    private final String login;
    private final String email;
    private final WebSocket clientSocket;
    private final ClientService service;

    public SendEmailThread(String login, String email, WebSocket clientSocket){
        this.login = login;
        this.email = email;
        this.clientSocket = clientSocket;
        service = GetSpringContext.getContext().getBean(ClientService.class);
    }

    @Override
    public void run(){
        service.sendMessageOnEmail(login, email, clientSocket);
    }
}
