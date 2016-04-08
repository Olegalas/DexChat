import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.dexchat.model.*;
import ua.dexchat.server.dao.BufferDao;
import ua.dexchat.server.service.ClientService;

import java.util.Date;

/**
 * Created by dexter on 07.04.16.
 */
public class TestBufferDao {

    private static final int TEST_ID_OWNER = 10;
    private static final int TEST_ID_SENDER = 100;
    private static final String TEST_MESSAGE_OLD = "test message";
    private static final String TEST_MESSAGE_NEW = "test message new";

    ApplicationContext context =
            new ClassPathXmlApplicationContext("/spring-context.xml");

    BufferDao buffDao = context.getBean(BufferDao.class);

    TemporaryBuffer expectedBuff;


    @After
    public void after() throws ClassNotFoundException {
        DropTables.dropTables();
    }

    @Before
    public void before() {
        expectedBuff = new TemporaryBuffer();
        expectedBuff.setIdOwnerr(TEST_ID_OWNER);
        Message message = new Message(TEST_MESSAGE_OLD, TEST_ID_SENDER, TEST_ID_OWNER, new Date());
        expectedBuff.getMessages().add(message);
        message.setTempBuffer(expectedBuff);
        buffDao.saveTempBuffer(expectedBuff);
    }

    @Test
    public void testSaveAndFindTemporaryBuffer(){

        TemporaryBuffer actualBuff = buffDao.findBufferByIdOwner(TEST_ID_OWNER);
        Assert.assertEquals(expectedBuff.getMessages().size(), actualBuff.getMessages().size());

    }

    @Test
    public void testRefreshTemporaryBuffer(){

        // create new message
        Message newMessage = new Message(TEST_MESSAGE_NEW, TEST_ID_SENDER, TEST_ID_OWNER, new Date());
        expectedBuff.getMessages().add(newMessage);
        newMessage.setTempBuffer(expectedBuff);


        buffDao.saveMessageInBuffer(newMessage);


        // get buffer after refresh
        TemporaryBuffer actualBuff = buffDao.findBufferByIdOwner(TEST_ID_OWNER);
        Assert.assertEquals(expectedBuff.getMessages().size(), actualBuff.getMessages().size());
    }

    @Test
    public void testUpdateMassage(){

        ClientService service = context.getBean(ClientService.class);

        Login clientLogin = new Login("name", "pass", "login");
        Login friendLogin = new Login("name2", "pass2", "login2");

        int idClient = service.saveClient(clientLogin);
        int idFriendClient = service.saveClient(friendLogin);

        TemporaryBuffer actualBuff = buffDao.findBufferByIdOwner(idClient);
        Client client = service.findClient(clientLogin);


        Message newMessage = new Message(TEST_MESSAGE_NEW, idFriendClient, idClient, new Date());
        newMessage.setTempBuffer(actualBuff);
        buffDao.saveMessageInBuffer(newMessage);

        actualBuff = buffDao.findBufferByIdOwner(idClient);
        Message actualMessageFromTempBuff = actualBuff.getMessages().get(0);

        History history = new History();
        history.setIdOwner(client);
        history.setIdSender(idFriendClient);
        client.getHistory().add(history);

        buffDao.saveNewMessageBuffer(history);

        Message fromMessageBuffer = new Message(actualMessageFromTempBuff);
        fromMessageBuffer.setHistory(history);
        buffDao.saveMessageInBuffer(fromMessageBuffer);

        client = service.findClient(clientLogin);

        Assert.assertEquals(TEST_MESSAGE_NEW, client.getHistory().get(0).getMessages().get(0).getMessage());
    }

    @Test
    public void testSaveNewMessageBuffer(){
        ClientService service = context.getBean(ClientService.class);
        Login login = new Login("name", "pass", "login");
        service.saveClient(login);
        Client friend = service.findClient(login);

        History history = new History();

        friend.getHistory().add(history);
        history.setIdOwner(friend);
        history.setIdSender(TEST_ID_SENDER);

        buffDao.saveNewMessageBuffer(history);

        friend = service.findClient(login);

        Assert.assertEquals(history.getId(), friend.getHistory().get(0).getId());
    }

    @Test
    public void testRemoveMassage(){

        Message messageToRemove = expectedBuff.getMessages().get(0);
        expectedBuff.getMessages().remove(0);
        buffDao.removeMessage(messageToRemove);

        TemporaryBuffer bufferFromDB = buffDao.findBufferByIdOwner(TEST_ID_OWNER);

        Assert.assertEquals(0, bufferFromDB.getMessages().size());
    }
}
