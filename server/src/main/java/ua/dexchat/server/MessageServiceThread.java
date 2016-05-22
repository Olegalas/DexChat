package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import ua.dexchat.model.Client;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;
import ua.dexchat.server.utils.WebSocketUtils;

/**
 * Created by dexter on 15.04.16.
 */
public class MessageServiceThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(MessageServiceThread.class);
    private final WebSocket clientSocket;
    private final Client client;
    private final ClientService service;

    public MessageServiceThread(Client client, WebSocket clientSocket){

        this.client = client;
        this.clientSocket = clientSocket;

        service = GetSpringContext.getContext().getBean(ClientService.class);
    }

    @Override
    public void run() {

        service.sendAllFriends(clientSocket, client);
        LOGGER.info("***All friends was sent");

        while(!isInterrupted()){

            service.sendMessagesFromTemporaryBufferToClient(clientSocket, client);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException " + e.getMessage());
                break;
            }

        }

        LOGGER.info("***Thread was killed");
    }
}
