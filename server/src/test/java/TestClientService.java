import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.dexchat.model.Login;
import ua.dexchat.model.Message;
import ua.dexchat.model.TemporaryBuffer;
import ua.dexchat.server.service.ClientService;

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

    @Before
    public void before(){
        idOwnerBuffer = service.saveClient(login);
    }

    @Test
    public void sendMessage(){

        Message testMessage = new Message(TEST_TEXT_MESSAGE, TEST_ID_SENDER, idOwnerBuffer, new Date());
        service.sendMessage(testMessage);

        TemporaryBuffer buff = service.findTemporaryBuffer(idOwnerBuffer);

        Assert.assertEquals(testMessage, buff.getMessages().get(0));
    }

}
