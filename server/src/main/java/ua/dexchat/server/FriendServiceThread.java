package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import ua.dexchat.model.ClientDTO;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.service.GetSpringContext;
import ua.dexchat.server.utils.WebSocketUtils;

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

            LOGGER.info("***Client " + clientSocket.getRemoteSocketAddress() + " " +
                    "want search new friends by \'" + clientDTO.getLogin() + "\'");
            // search new friends among clients database
            List<ClientDTO> clients;
            clients = service.findPotentialFriends(clientDTO.getLogin(), 10);

            if(clients.isEmpty()){
                WebSocketUtils.sendTextMessageToClient("incorrect friend login", clientSocket);
            }else{
                service.sendAllFriends(clientSocket, clients);
            }

        } else{
            LOGGER.info("***Client " + clientSocket.getRemoteSocketAddress() + " " +
                    "want add new friend \'" + clientDTO.getFriends().get(0).getLogin() + "\'");

            service.saveNewFriend(clientDTO);
        }

    }
}
