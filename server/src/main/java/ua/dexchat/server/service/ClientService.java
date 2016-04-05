package ua.dexchat.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.dexchat.model.Message;
import ua.dexchat.model.TemporaryBuffer;
import ua.dexchat.server.dao.TemporaryBufferDao;

/**
 * Created by dexter on 05.04.16.
 */
@Service
public class ClientService {

    @Autowired
    private TemporaryBufferDao temporaryBufferDao;

    public void sendMessage(Message message){
        TemporaryBuffer buffer = temporaryBufferDao.findBuffer(message.getIdReceiver());
        buffer.getMessages().add(message);
    }
}
