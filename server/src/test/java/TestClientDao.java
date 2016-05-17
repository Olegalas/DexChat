import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.dexchat.model.Client;
import ua.dexchat.model.Login;
import ua.dexchat.server.dao.ClientDao;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created by dexter on 04.04.16.
 */
public class TestClientDao {

    private static final Logger LOGGER = Logger.getLogger(TestClientDao.class);

    private static final int UNEXPECTED_ID = 10000;
    private static final int MAX_SIZE_OF_QUERY = 5;
    private static final int EMPTY_FRIENDS_LIST = 0;
    private static final String WRONG_LOGIN = "wrong login";
    private static final String WRONG_PASS = "wrong pass";

    private Login login;
    private Client client;

    private final ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
    private final ClientDao clientDao = context.getBean(ClientDao.class);

    @After
    public void tearDown() throws ClassNotFoundException {
        DropTables.dropTables();
    }

    @Before
    public void saveLogin(){

        login = new Login("Dan", "pass", "login", "127.0.0.1", "email2@ukr.net");
        clientDao.saveClient(login);

        client = new Client("loginForClient", "James", "pass", "email@ukr.net");
        clientDao.saveClient(client);

    }

    @Test
    public void findClientByLoginObject(){

        Client clientFromDB = clientDao.findClient(login);
        Assert.assertEquals(clientFromDB.getLogin(), login.login);

    }

    @Test
    public void findClientByLoginObjectWrongPass(){

        login.pass = WRONG_PASS;
        Client clientFromDB = clientDao.findClient(login);
        Assert.assertNull(clientFromDB);

    }

    @Test
    public void findClientByLoginObjectWrongLogin(){

        login.login = WRONG_LOGIN;
        Client clientFromDB = clientDao.findClient(login);
        Assert.assertNull(clientFromDB);

    }

    @Test
    public void findClientById(){

        Client clientFromDB = clientDao.findClient(client.getId());
        Assert.assertEquals(clientFromDB.getLogin(), client.getLogin());

    }

    @Test
    public void findClientByIdWrongId(){

        Client clientFromDB = clientDao.findClient(UNEXPECTED_ID);
        Assert.assertNull(clientFromDB);

    }

    @Test
    public void findClientByLogin(){

        Client clientFromDB = clientDao.findClientByLogin(login.login);
        Assert.assertEquals(clientFromDB.getLogin(), login.login);

    }

    @Test(expected = NoResultException.class)
    public void findClientByLoginNegative(){

        login.login = WRONG_LOGIN;
        Client clientFromDB = clientDao.findClientByLogin(login.login);
        Assert.assertNull(clientFromDB);

    }

    @Test
    public void findClientsByLogin(){

        List<Client> clients = clientDao.findClientsByLogin(login.login, MAX_SIZE_OF_QUERY);
        Assert.assertEquals(2, clients.size());

    }

    @Test
    public void findClientsByLoginNegative(){

        List<Client> clients = clientDao.findClientsByLogin(WRONG_LOGIN, MAX_SIZE_OF_QUERY);
        Assert.assertEquals(EMPTY_FRIENDS_LIST, clients.size());

    }

    @Test
    public void addFriendToClient(){

        clientDao.addFriendToClient(client.getLogin(), login.login);
        Client clientFromDB = clientDao.findClient(client.getId());

        Assert.assertEquals(clientFromDB.getMyFriends().get(0).getLogin(), login.login);

    }

    @Test(expected = NoResultException.class)
    public void addFriendToClient_WrongFriendLogin(){

        clientDao.addFriendToClient(client.getLogin(), WRONG_LOGIN);
        Client clientFromDB = clientDao.findClient(client.getId());

        Assert.assertEquals(clientFromDB.getMyFriends().size(), EMPTY_FRIENDS_LIST);

    }

    @Test(expected = NoResultException.class)
    public void addFriendToClient_WrongClientLogin(){

        clientDao.addFriendToClient(WRONG_LOGIN, client.getLogin());
        Client clientFromDB = clientDao.findClient(client.getId());

        Assert.assertEquals(clientFromDB.getMyFriends().size(), EMPTY_FRIENDS_LIST);
    }

    @Test
    public void removeFriendFromClient(){

        clientDao.addFriendToClient(client.getLogin(), login.login);
        clientDao.removeFriendFromClient(client.getLogin(), login.login);

        Client clientFromDB = clientDao.findClient(client.getId());
        Assert.assertTrue(clientFromDB.getMyFriends().isEmpty());

    }

    @Test(expected = NoResultException.class)
    public void removeFriendFromClient_WrongFriendLogin(){

        clientDao.addFriendToClient(client.getLogin(), login.login);
        clientDao.removeFriendFromClient(client.getLogin(), WRONG_LOGIN);

//        Client clientFromDB = clientDao.findClient(client.getId());
//        Assert.assertEquals(clientFromDB.getMyFriends().get(0).getLogin(), login.login);

    }

}
