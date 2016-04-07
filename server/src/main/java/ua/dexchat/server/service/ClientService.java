package ua.dexchat.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import ua.dexchat.model.Client;
import ua.dexchat.model.Login;
import ua.dexchat.model.Message;
import ua.dexchat.model.TemporaryBuffer;
import ua.dexchat.server.dao.ClientDao;
import ua.dexchat.server.dao.TemporaryBufferDao;

/**
 * Created by dexter on 05.04.16.
 */
@Service
public class ClientService {

    @Autowired
    private TemporaryBufferDao temporaryBufferDao;

    @Autowired
    private ClientDao clientDao;

    public void sendMessage(Message message){
        TemporaryBuffer buffer = temporaryBufferDao.findBufferByIdOwner(message.getIdReceiver());
        buffer.getMessages().add(message);
//        temporaryBufferDao.refreshBuffer(buffer);
    }

    public TemporaryBuffer findTemporaryBuffer(int idOwner){
        return temporaryBufferDao.findBufferByIdOwner(idOwner);
    }

    public int saveClient(Login login){
        Client client = new Client(login);

        int idClient = clientDao.saveClient(client);

        if(idClient == -1){
            return -1;
        }

        ApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-context.xml");

        TemporaryBufferDao tempBuffDao = context.getBean(TemporaryBufferDao.class);
        TemporaryBuffer buff = new TemporaryBuffer();
        buff.setIdOwnerr(client.getId());
        tempBuffDao.saveTempBuffer(buff);

        return idClient;
    }

    public Client findClient(Login login){
        return clientDao.findClient(login);
    }
}
