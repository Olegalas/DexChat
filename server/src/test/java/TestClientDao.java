import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.dexchat.model.Client;
import ua.dexchat.model.Login;
import ua.dexchat.server.dao.ClientDao;

/**
 * Created by dexter on 04.04.16.
 */
public class TestClientDao {

    private static Logger LOGGER = Logger.getLogger(TestClientDao.class);

    private Login login = new Login("name", "pass", "login");
    ApplicationContext context =
            new ClassPathXmlApplicationContext("/spring-context.xml");


    @After
    public void tearDown() throws ClassNotFoundException {
        DropTables.dropTables();
    }

    @Test
    public void testSaveAndFindClientByLoginPositive(){

        // if we want to find our client, first we must save the client
        ClientDao clientDao = context.getBean(ClientDao.class);
        int idActualClient = clientDao.saveClient(login);

        // then we create our client and equals him with client from data base
        Client expectedClient = clientDao.findClient(login);

        Assert.assertEquals(idActualClient, expectedClient.getId());
    }

    @Test
    public void testSaveDuplicateClient(){

        // if we want to find our client, first we must save the client
        ClientDao clientDao = context.getBean(ClientDao.class);

        clientDao.saveClient(login);

        int negativeResult = -1;
        try{
            negativeResult = clientDao.saveClient(login);
        }catch (Exception ignore){/*NOP*/}

        Assert.assertEquals(-1, negativeResult);
    }

    @Test
    public void testFindClientByLoginInEmptyTables(){

        ClientDao clientDao = context.getBean(ClientDao.class);
        // test with empty data base
        Client expectedClient = clientDao.findClient(login);

        Assert.assertNull(expectedClient);
    }

    @Test
    public void testFindUnknownClient(){

        // if we want to find our client, first we must save the client
        ClientDao clientDao = context.getBean(ClientDao.class);
        clientDao.saveClient(login);

        // then we create our unknown client
        Login unknownLogin = new Login("unknown","unknown","unknown");

        Client expectedClient = clientDao.findClient(unknownLogin);

        Assert.assertNull(expectedClient);
    }
}
