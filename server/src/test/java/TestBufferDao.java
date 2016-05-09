import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionSystemException;
import ua.dexchat.model.*;
import ua.dexchat.server.dao.BufferDao;
import ua.dexchat.server.dao.ClientDao;
import ua.dexchat.server.service.ClientService;

import java.util.Date;

/**
 * Created by dexter on 07.04.16.
 */
public class TestBufferDao {

    private static final String TEXT_MESSAGE = "tex message";
    private static final int ID_OWNER = 20;
    private static final int ID_SENDER = 10;
    private static final int UNEXPECTED_ID = 10000;

    private TemporaryBuffer buff;
    private int idMessage;

    private final ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
    private final BufferDao bufferDao = context.getBean(BufferDao.class);



    @Before
    public void saveBuffer(){

        buff = new TemporaryBuffer();
        buff.setIdOwnerr(ID_OWNER);

        bufferDao.saveTempBuffer(buff);
        Message newMessage = new Message(TEXT_MESSAGE, ID_SENDER, ID_OWNER, new Date());
        newMessage.setTempBuffer(buff);
        bufferDao.saveMessageInBuffer(newMessage);
        idMessage = newMessage.getId();
    }

    @After
    public void after() throws ClassNotFoundException {
        DropTables.dropTables();
    }

    @Test
    public void testFindBufferByIdOwner(){
        TemporaryBuffer bufFromDB = bufferDao.findBufferByIdOwner(ID_OWNER);
        Assert.assertEquals(bufFromDB, buff);
    }

    @Test
    public void testFindBufferByIdOwnerNegative(){
        TemporaryBuffer bufFromDB = bufferDao.findBufferByIdOwner(UNEXPECTED_ID);
        Assert.assertNull(bufFromDB);
    }

    @Test
    public void testFindBufferById(){
        TemporaryBuffer bufFromDB = bufferDao.findBufferById(buff.getId());
        Assert.assertEquals(bufFromDB, buff);
    }

    @Test
    public void testFindBufferByIdNegative(){
        TemporaryBuffer bufFromDB = bufferDao.findBufferById(UNEXPECTED_ID);
        Assert.assertNull(bufFromDB);
    }

    @Test
    public void testRemoveBuffer(){
        bufferDao.removeBuffer(buff);
        TemporaryBuffer bufFromDB = bufferDao.findBufferByIdOwner(ID_OWNER);
        Assert.assertNull(bufFromDB);
    }

    @Test(expected = TransactionSystemException.class)
    public void testRemoveBufferNegative(){
        bufferDao.removeBuffer(new TemporaryBuffer());
        TemporaryBuffer bufFromDB = bufferDao.findBufferByIdOwner(ID_OWNER);
        Assert.assertNull(bufFromDB);
    }

    @Test
    public void testRemoveMessage(){
        buff = bufferDao.findBufferByIdOwner(ID_OWNER);
        Assert.assertEquals(1, buff.getMessages().size()); // one message in temporary buffer before remove
        Message messageToRemove = new Message();
        messageToRemove.setId(idMessage);
        bufferDao.removeMessage(messageToRemove);
        buff = bufferDao.findBufferByIdOwner(ID_OWNER);
        Assert.assertEquals(0, buff.getMessages().size()); // empty temporary buffer after remove
    }

    @Test
    public void testRemoveMessageNegative(){
        Message defunct = new Message();
        defunct.setId(UNEXPECTED_ID);
        bufferDao.removeMessage(defunct); //this method doesn't delete anything.. but exception it doesn't throw too
    }

    @Test
    public void testSaveMessageInBuffer(){
        Message newMessage = new Message(TEXT_MESSAGE, ID_SENDER, ID_OWNER, new Date());
        newMessage.setTempBuffer(buff);
        bufferDao.saveMessageInBuffer(newMessage);
        buff = bufferDao.findBufferByIdOwner(ID_OWNER);
        Assert.assertEquals(buff.getMessages().size(), 2); // we put second message, that't why we have size - 2
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveMessageInBufferNegative(){
        bufferDao.saveMessageInBuffer(null);
        buff = bufferDao.findBufferByIdOwner(ID_OWNER);
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveMessageInBufferNegativeDuplicating(){
        Message newMessageFirst = new Message(TEXT_MESSAGE, ID_SENDER, ID_OWNER, new Date());
        newMessageFirst.setTempBuffer(buff);
        bufferDao.saveMessageInBuffer(newMessageFirst);
        bufferDao.saveMessageInBuffer(newMessageFirst); // resave - will throw TransactionSystemException.class
     }

    @Test
    public void testSaveNewHistoryAndFind(){
        History history = new History();

        Client owner = new Client("owner", "owner", "owner", "email@ukr.net");
        Client sender = new Client("sender", "sender", "sender", "email2@ukr.net");

        history.setIdOwner(owner);
        history.setIdSender(sender.getId());

        ClientDao clientDao = context.getBean(ClientDao.class);

        clientDao.saveClient(owner);
        clientDao.saveClient(sender);
        bufferDao.saveNewHistory(history);
        History historyFromDB = bufferDao.findHistoryById(history.getId());

        Assert.assertEquals(history, historyFromDB);
    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveNewHistoryNegativeDuplicating(){
        History history = new History();

        Client owner = new Client("owner", "owner", "owner", "email@ukr.net");
        Client sender = new Client("sender", "sender", "sender", "email2@ukr.net");

        history.setIdOwner(owner);
        history.setIdSender(sender.getId());

        ClientDao clientDao = context.getBean(ClientDao.class);

        clientDao.saveClient(owner);
        clientDao.saveClient(sender);
        bufferDao.saveNewHistory(history);
        bufferDao.saveNewHistory(history); // resave - will throw TransactionSystemException.class

    }

    @Test(expected = TransactionSystemException.class)
    public void testSaveNewHistoryNegativeNull(){
        bufferDao.saveNewHistory(null);
    }
}
