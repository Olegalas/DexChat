import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

        LOGGER.info("****************manager was created");

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

        Class.forName("com.mysql.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dexchat", "root", "root")) {

            Statement statement = connection.createStatement();
            statement.execute("drop table hibernate_sequence");
            statement.execute("drop table clients_clients");
            statement.execute("drop table messages");
            statement.execute("drop table Message_buffer");
            statement.execute("drop table clients");
            LOGGER.info("********DataBase was removed");

        } catch (SQLException e) {
            LOGGER.error("Error connection");
        }

    }

    @Test
    public void testPersist(){

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dexunit");
        EntityManager manager = entityManagerFactory.createEntityManager();

        try{

            manager.getTransaction().begin();
            manager.persist(client);
            manager.getTransaction().commit();

        }catch (Exception e){
            LOGGER.info("****************test client has already created before");
        }

        manager.close();

        manager = entityManagerFactory.createEntityManager();

        manager.getTransaction().begin();
        Client clientFromDB = manager.find(Client.class, client.getId());
        manager.getTransaction().commit();
        manager.close();

        Assert.assertEquals(clientFromDB, client);
        String messageFromDB = clientFromDB.getBuffers().get(0).getMessages().get(0).getMessage();
        Assert.assertEquals(messageFromDB, message);

        LOGGER.debug("load test client = " + clientFromDB);
        LOGGER.debug("message = " + messageFromDB);

    }
}
