package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import ua.dexchat.model.Client;
import ua.dexchat.model.ClientDTO;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;

import java.util.List;

/**
 * Created by dexter on 27.04.16.
 */
public class FriendServiceThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(FriendServiceThread.class);

    private final ClientDTO clientDTO;
    private final WebSocket clientSocket;
    private final ClientService service;


    public FriendServiceThread(ClientDTO clientDTO, WebSocket client) {
        this.clientDTO = clientDTO;
        this.clientSocket = client;

        service = GetSpringContext.getContext().getBean(ClientService.class);
    }

    @Override
    public void run(){

        // if DTO have empty list... it means that client want to find new friends by login
        // else... it means that client want to add new friend
        if(clientDTO.getFriends().isEmpty()){

            List<ClientDTO> clients;
            clients = service.findFriends(clientDTO.getLogin(), 25);

            // TODO: 27.04.16 send friends by ClientService if list is'n empty

        } else{
            // TODO: 27.04.16 add new find to Client friends list by ClientService
        }

    }
}
