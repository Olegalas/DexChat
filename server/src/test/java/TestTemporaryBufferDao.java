import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.dexchat.model.Message;
import ua.dexchat.model.TemporaryBuffer;
import ua.dexchat.server.dao.TemporaryBufferDao;

import java.util.Date;

/**
 * Created by dexter on 07.04.16.
 */
public class TestTemporaryBufferDao {

    private static final int TEST_ID_OWNER = 10;
    private static final int TEST_ID_SENDER = 100;
    private static final String TEST_MESSAGE_OLD = "test message";
    private static final String TEST_MESSAGE_NEW = "test message new";

    ApplicationContext context =
            new ClassPathXmlApplicationContext("/spring-context.xml");

    TemporaryBufferDao buffDao = context.getBean(TemporaryBufferDao.class);

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
    }

    @Test
    public void testSaveAndFindTemporaryBuffer(){

        buffDao.saveTempBuffer(expectedBuff);
        TemporaryBuffer actualBuff = buffDao.findBufferByIdOwner(TEST_ID_OWNER);
        Assert.assertEquals(expectedBuff.getMessages().size(), actualBuff.getMessages().size());

    }

    @Test
    public void testRefreshTemporaryBuffer(){

        buffDao.saveTempBuffer(expectedBuff);

        // create new message
        Message newMessage = new Message(TEST_MESSAGE_NEW, TEST_ID_SENDER, TEST_ID_OWNER, new Date());
        expectedBuff.getMessages().add(newMessage);
        newMessage.setTempBuffer(expectedBuff);


        buffDao.saveMessageInBuffer(newMessage);


        // get buffer after refresh
        TemporaryBuffer actualBuff = buffDao.findBufferByIdOwner(TEST_ID_OWNER);
        Assert.assertEquals(expectedBuff.getMessages().size(), actualBuff.getMessages().size());
    }
}
