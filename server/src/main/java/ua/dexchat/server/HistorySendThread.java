package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;

/**
 * Created by dexter on 22.05.16.
 */
public class HistorySendThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(HistorySendThread.class);
    private final WebSocket clientSocket;
    private final String loginFriend;
    private final String loginClient;
    private final ClientService service;


    public HistorySendThread(WebSocket clientSocket, String loginFriend, String loginClient){
        this.clientSocket = clientSocket;
        this.loginFriend = loginFriend;
        this.loginClient = loginClient;
        service = GetSpringContext.getContext().getBean(ClientService.class);
    }

    @Override
    public void run(){
        service.sendHistory(clientSocket,loginClient, loginFriend, 20);
        LOGGER.info("***Lust history was sent");
    }
}
