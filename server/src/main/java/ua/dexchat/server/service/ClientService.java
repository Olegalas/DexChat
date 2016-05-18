package ua.dexchat.server.service;

import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.dexchat.model.*;
import ua.dexchat.server.dao.ClientDao;
import ua.dexchat.server.dao.BufferDao;
import ua.dexchat.server.exceptions.WasNotFoundException;
import ua.dexchat.server.utils.WebSocketUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexter on 05.04.16.
 */
@Service
public class ClientService {

    private static final Logger LOGGER = Logger.getLogger(ClientService.class);

    @Autowired
    private BufferDao bufferDao;

    @Autowired
    private ClientDao clientDao;

    public void sendMessageToFriend(Message message){
        TemporaryBuffer buffer =  bufferDao.findBufferByIdOwner(message.getIdReceiver());
        if(buffer == null) throw new WasNotFoundException("incorrect id receiver");
        message.setTempBuffer(buffer);
        bufferDao.saveMessageInBuffer(message);
        saveMessageInHistory(message.getIdSender(), message.getIdReceiver(), message);
    }

    public void saveMessageInHistory(int idSenderInBuffer, int idBufferOwner, Message message){

        Client client = clientDao.findClient(idBufferOwner);

        for(History history : client.getHistory()){
            if(history.getIdSender() == idSenderInBuffer){
                Message newMessage = new Message(message);
                newMessage.setHistory(history);
                bufferDao.saveMessageInBuffer(newMessage);
                LOGGER.info("***Output message was save in history : " + newMessage);
                return;
            }
        }

        History newHistory = new History();

        newHistory.setIdOwner(client);
        newHistory.setIdSender(idSenderInBuffer);

        Client friend = clientDao.findClient(idSenderInBuffer);
        newHistory.setLoginSender(friend.getLogin());

        client.getHistory().add(newHistory);
        bufferDao.saveNewHistory(newHistory);
        LOGGER.info("***New history for new friend was created");
        saveMessageInHistory(idBufferOwner, idSenderInBuffer, message);

    }

    public void sendAllHistoryToClient(WebSocket clientSocket, Client client){
        WebSocketUtils.sendHistoryToClient(client, clientSocket);
    }

    public void sendMessagesFromTemporaryBufferToClient(WebSocket clientSocket, Client client){
        TemporaryBuffer buff = findTemporaryBuffer(client.getId());

        for(Message message : buff.getMessages()){
            WebSocketUtils.sendMessageToClient(message, clientSocket);
            saveMessageInHistory(message.getIdReceiver(), message.getIdSender(), message);
            removeMessageFromTempBuff(message);
        }
    }

    public TemporaryBuffer findTemporaryBuffer(int idOwner){
        return bufferDao.findBufferByIdOwner(idOwner);
    }

    public int saveClient(Login login){
        Client client = new Client(login);

        clientDao.saveClient(client);

        Client clientFromDB = clientDao.findClient(client.getId());

        TemporaryBuffer buff = new TemporaryBuffer();
        buff.setIdOwnerr(clientFromDB.getId());
        bufferDao.saveTempBuffer(buff);

        return clientFromDB.getId();
    }

    public Client findClientByLoginObject(Login login){
        return clientDao.findClient(login);
    }

    private void removeMessageFromTempBuff(Message message) {
          bufferDao.removeMessage(message);
    }

    public List<ClientDTO> findPotentialFriends(String friendsLoginStartsWith, int maxListSize){

        List<Client> clients;
        List<ClientDTO> clientsDTO = new ArrayList<>();

        clients = clientDao.findClientsByLogin(friendsLoginStartsWith, maxListSize);

        for(Client client : clients){
            clientsDTO.add(ClientDTO.getClientDTOWithoutFriends(client));
        }

        return clientsDTO;
    }


    // if I send clientsDTO with full list.. that's mean I send client with his friends
    public void sendAllFriends(WebSocket clientSocket, Client client) {
        WebSocketUtils.sendFriendsMessageToClient(ClientDTO.getClientDTOWithFriends(client), clientSocket);
    }

    // if I send clientsDTO with empty list.. that's mean I send new friend
    public void sendAllFriends(WebSocket clientSocket, List<ClientDTO> friends) {
        for(ClientDTO friend : friends){
            WebSocketUtils.sendFriendsMessageToClient(friend, clientSocket);
        }
    }

    public void saveNewFriend(ClientDTO clientDTO) {
        clientDao.addFriendToClient(clientDTO.getLogin(), clientDTO.getFriends().get(0).getLogin());
    }

    public Client findByLogin(String login){
        return clientDao.findClientByLogin(login);
    }

    public void removeFriend(String loginClient, String loginFriend){
        clientDao.removeFriendFromClient(loginClient, loginFriend);
    }
}
