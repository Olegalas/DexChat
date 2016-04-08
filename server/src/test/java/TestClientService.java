import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.dexchat.model.Client;
import ua.dexchat.model.Login;
import ua.dexchat.model.Message;
import ua.dexchat.model.TemporaryBuffer;
import ua.dexchat.server.service.ClientService;
import ua.dexchat.server.stream.StreamUtils;

import java.util.Date;

/**
 * Created by dexter on 07.04.16.
 */
public class TestClientService {

    private static final String TEST_TEXT_MESSAGE = "some test text";
    private static final int TEST_ID_SENDER = 10;
    private int idOwnerBuffer;

    private static final ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
    private static final ClientService service = context.getBean(ClientService.class);

    private static final Login login = new Login("name", "pass", "login");

    @After
    public void after() throws ClassNotFoundException {
        DropTables.dropTables();
    }

    @Test
    public void testClientService() {
        idOwnerBuffer = service.saveClient(login);

        Client clientFromDB = service.findClient(login);

        Assert.assertEquals(idOwnerBuffer, clientFromDB.getId());

        Message testMessage = new Message(TEST_TEXT_MESSAGE, TEST_ID_SENDER, idOwnerBuffer, new Date());
        service.sendMessageToFriend(testMessage);

        TemporaryBuffer buff = service.findTemporaryBuffer(idOwnerBuffer);

        Assert.assertEquals(testMessage, buff.getMessages().get(0));
    }

    @Test
    public void testSaveInHistory(){
        idOwnerBuffer = service.saveClient(login);
        Client clientFromDB = service.findClient(login);
        Message testMessage = new Message(TEST_TEXT_MESSAGE, TEST_ID_SENDER, idOwnerBuffer, new Date());
        service.sendMessageToFriend(testMessage);

        TemporaryBuffer buff = service.findTemporaryBuffer(idOwnerBuffer);
        for (Message message : buff.getMessages()){
            service.saveMessageInHistory(clientFromDB, message);
            service.removeMessageFromTempBuff(message);
        }

        buff = service.findTemporaryBuffer(idOwnerBuffer);
        Assert.assertEquals(0, buff.getMessages().size());

        clientFromDB = service.findClient(login);

        Assert.assertEquals(1 , clientFromDB.getHistory().get(0).getMessages().size());
    }
}
