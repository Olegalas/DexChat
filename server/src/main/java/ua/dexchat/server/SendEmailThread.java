package ua.dexchat.server;

import org.java_websocket.WebSocket;
import ua.dexchat.server.utils.EmailUtils;

/**
 * Created by dexter on 09.05.16.
 */
public class SendEmailThread extends Thread {

    private final String login;
    private final String email;
    private final WebSocket clientSocket;

    public SendEmailThread(String login, String email, WebSocket clientSocket){
        this.login = login;
        this.email = email;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run(){
        EmailUtils.sendMessage(login, email, clientSocket);
    }
}
