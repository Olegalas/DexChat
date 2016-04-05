import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.dexchat.model.Client;
import ua.dexchat.model.Message;
import ua.dexchat.model.MessageBuffer;
import ua.dexchat.server.dao.ClientDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

/**
 * Created by dexter on 01.04.16.
 */
public class TestDataBase {

    private final static Logger LOGGER = Logger.getLogger(TestDataBase.class);
    private Client client;
    private String message;

    @Before
    public void setUp(){

        client = new Client("client", "client", "client");
        message = "testMessage";

        MessageBuffer messageBuffer = new MessageBuffer();
        Message testMessage = new Message(message, client.getId(), client.getId(), new Date(), messageBuffer);

        messageBuffer.getMessages().add(testMessage);
        messageBuffer.setIdOwner(client);
        messageBuffer.setIdSender(client.getId());

        client.getBuffers().add(messageBuffer);

    }

    @After
    public void tearDown() throws ClassNotFoundException {
        DropTables.dropTables();
    }

    @Test
    public void testPersist(){
        ApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-context.xml");

        ClientDao clientDao = context.getBean(ClientDao.class);

        clientDao.saveClient(client);

        Client clientFromDB = clientDao.findClient(client.getId());

        Assert.assertEquals(clientFromDB, client);
        String messageFromDB = clientFromDB.getBuffers().get(0).getMessages().get(0).getMessage();
        Assert.assertEquals(messageFromDB, message);

        LOGGER.debug("***load test client = " + clientFromDB);
        LOGGER.debug("***message = " + messageFromDB);
    }
}
