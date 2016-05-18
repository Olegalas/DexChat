package ua.dexchat.server;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import ua.dexchat.model.Client;
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
        // if email - "delete" it's mean that client want to delete friend with next login
        if(clientDTO.getFriends().isEmpty()){

            if("delete".equals(clientDTO.getEmail())){
                // name field - login client // login field - login friend
                service.removeFriend(clientDTO.getName(), clientDTO.getLogin());
                WebSocketUtils.sendTextMessageToClient("friend - " + clientDTO.getLogin() + " was delete", clientSocket);
            }else{
                List<ClientDTO> clients = clientsSearch();

                if(clients.isEmpty()){
                    LOGGER.info("***No clients with \"" + clientDTO.getLogin() + "\" login were found");
                    WebSocketUtils.sendTextMessageToClient("incorrect friend login", clientSocket);
                }else{
                    LOGGER.info("***Were found next clients : " + clients.toString());
                    service.sendAllFriends(clientSocket, clients);
                }
            }

        } else{
            LOGGER.info("***Client " + clientSocket.getRemoteSocketAddress() + " " +
                    "want add new friend \'" + clientDTO.getFriends().get(0).getLogin() + "\'");

            service.saveNewFriend(clientDTO);
        }

    }

    private List<ClientDTO> clientsSearch() {

        LOGGER.info("***Client " + clientSocket.getRemoteSocketAddress() + " " +
                "want search new friends by \'" + clientDTO.getLogin() + "\'");

        // search new friends among clients database
        List<ClientDTO> clients;

        // in DTO login filed - this is login friends for search
        clients = service.findPotentialFriends(clientDTO.getLogin(), 10);

        // in DTO name field - this is login of client
        Client client = service.findByLogin(clientDTO.getName());

        // remove duplicate
        for(Client friend : client.getMyFriends()){
            for(int i = 0; i < clients.size(); i++){
                if(friend.getLogin().equals(clients.get(i).getLogin())){
                    clients.remove(i);
                }
            }
        }
        return clients;
    }
}
