import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.dexchat.model.*;
import ua.dexchat.server.service.ClientService;

import java.util.Date;
import java.util.List;

/**
 * Created by dexter on 07.04.16.
 */
public class TestClientService {

    private static final int UNEXPECTED_ID = 10000;
    private static final String WRONG_PASS = "wrong pass";
    private static final String LOGIN_STARTS_WITH = "Login";
    private static final String WRONG_LOGIN = "wrong ownerLogin";
    private static final String TEST_TEXT_MESSAGE = "some test text";
    private static final int MAX_SIZE_OF_QUERY = 5;
    private static final int EXPECTED_SIZE_OF_FRIENDS_LIST = 2;
    private static final int EMPTY_FRIENDS_LIST = 0;
    private static final int TEST_ID_SENDER = 10;
    private int idOwnerBuffer;

    private final ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
    private final ClientService service = context.getBean(ClientService.class);

    private Login ownerLogin;
    private Login secondLogin;

    @After
    public void after() throws ClassNotFoundException {
        DropTables.dropTables();
    }

    @Before
    public void saveInDB(){

        ownerLogin = new Login("Tomas", "pass", "LoginFirst", "127.0.0.1", "email1@ukr.net");
        idOwnerBuffer = service.saveClient(ownerLogin);

        secondLogin = new Login("Tomas", "pass", "LoginSecond", "127.0.0.1", "email2@ukr.net");
        service.saveClient(secondLogin);

    }

    @Test
    public void findClientByLoginObject(){

        Client clientFromDB = service.findClientByLoginObject(ownerLogin);
        Assert.assertEquals(clientFromDB.getLogin(), ownerLogin.login);

    }

    @Test
    public void findClientByLoginObjectWrongPass(){

        ownerLogin.pass = WRONG_PASS;
        Client clientFromDB = service.findClientByLoginObject(ownerLogin);
        Assert.assertNull(clientFromDB);

    }

    @Test
    public void findClientByLoginObjectWrongLogin(){

        ownerLogin.login = WRONG_LOGIN;
        Client clientFromDB = service.findClientByLoginObject(ownerLogin);
        Assert.assertNull(clientFromDB);

    }

    @Test
    public void findByLogin(){

        Client clientFromDB = service.findByLogin(ownerLogin.login);
        Assert.assertEquals(clientFromDB.getLogin(), ownerLogin.login);

    }

    @Test
    public void findByLoginNegative(){

        Client clientFromDB = service.findByLogin(WRONG_LOGIN);
        Assert.assertNull(clientFromDB);

    }

    @Test
    public void findPotentialFriends(){

        List<ClientDTO> clients = service.findPotentialFriends(LOGIN_STARTS_WITH, MAX_SIZE_OF_QUERY);
        Assert.assertEquals(clients.size(), EXPECTED_SIZE_OF_FRIENDS_LIST);

    }

    @Test
    public void findPotentialFriendsWrongLoginQuery(){

        List<ClientDTO> clients = service.findPotentialFriends(WRONG_LOGIN, MAX_SIZE_OF_QUERY);
        Assert.assertEquals(clients.size(), EMPTY_FRIENDS_LIST);

    }

    @Test
    public void findTemporaryBuffer(){

        TemporaryBuffer buffFromDB = service.findTemporaryBuffer(idOwnerBuffer);
        Assert.assertEquals(buffFromDB.getIdOwnerr(), idOwnerBuffer);

    }

    @Test
    public void findTemporaryBufferNegative(){

        TemporaryBuffer buffFromDB = service.findTemporaryBuffer(UNEXPECTED_ID);
        Assert.assertNull(buffFromDB);

    }

    @Test
    public void sendMessageToFriend(){

        Message message = new Message(TEST_TEXT_MESSAGE, TEST_ID_SENDER, idOwnerBuffer, new Date());
        service.sendMessageToFriend(message);
        TemporaryBuffer buffFromDB = service.findTemporaryBuffer(idOwnerBuffer);
        Assert.assertEquals(TEST_TEXT_MESSAGE, buffFromDB.getMessages().get(0).getMessage());

    }

    @Test
    public void sendMessageToFriendNegative(){

        Message message = new Message(TEST_TEXT_MESSAGE, TEST_ID_SENDER, UNEXPECTED_ID, new Date());
        service.sendMessageToFriend(message); // AOP will be catching WasNotFoundException

    }

    @Test
    public void saveMessageInHistory(){

        Message message = new Message(TEST_TEXT_MESSAGE, TEST_ID_SENDER, idOwnerBuffer, new Date());
        service.saveMessageInHistory(message.getIdReceiver(), message.getIdSender(), message);
        Client client = service.findByLogin(ownerLogin.login);

        Assert.assertEquals(TEST_TEXT_MESSAGE, client.getHistory().get(0).getMessages().get(0).getMessage());

    }


}
