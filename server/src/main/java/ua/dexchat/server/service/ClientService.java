package ua.dexchat.server.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.dexchat.model.*;
import ua.dexchat.server.dao.ClientDao;
import ua.dexchat.server.dao.BufferDao;

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
        message.setTempBuffer(buffer);
        bufferDao.saveMessageInBuffer(message);
    }

    public TemporaryBuffer findTemporaryBuffer(int idOwner){
        return bufferDao.findBufferByIdOwner(idOwner);
    }

    public int saveClient(Login login){
        Client client = new Client(login);

        int idClient = clientDao.saveClient(client);

        if(idClient == -1){
            return -1;
        }

        TemporaryBuffer buff = new TemporaryBuffer();
        buff.setIdOwnerr(client.getId());
        bufferDao.saveTempBuffer(buff);

        return idClient;
    }

    public Client findClient(Login login){
        return clientDao.findClient(login);
    }

    public void saveMessageInHistory(Client client, Message message){

        for(History history : client.getHistory()){
            if(history.getIdSender() == message.getIdSender()){
                Message newMessage = new Message(message);
                newMessage.setHistory(history);
                bufferDao.saveMessageInBuffer(newMessage);
                LOGGER.info("***Message was save in history : " + newMessage);
                return;
            }
        }

        History newHistory = new History();
        newHistory.setIdOwner(client);
        newHistory.setIdSender(message.getIdSender());
        client.getHistory().add(newHistory);
        bufferDao.saveNewMessageBuffer(newHistory);
        LOGGER.info("***New history for new friend was created");
        saveMessageInHistory(client, message);

    }

    public void removeMessageFromTempBuff(Message message) {
          bufferDao.removeMessage(message);
    }
}
