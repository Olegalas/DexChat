import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("dexTest");
        EntityManager manager = entityManagerFactory.createEntityManager();

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
    public void tearDown(){
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
        session.createSQLQuery("drop table hibernate_sequence;");
        session.createSQLQuery("drop table clients_clients;");
        session.createSQLQuery("drop table messages;");
        session.createSQLQuery("drop table Message_buffer;");
        session.createSQLQuery("drop table clients;");
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

        try{

            manager.getTransaction().begin();
            Client clientFromDB = manager.find(Client.class, client.getId());
            manager.getTransaction().commit();
            manager.close();

           // Assert.assertEquals(clientFromDB, client);
            String messageFromDB = clientFromDB.getBuffers().get(0).getMessages().get(0).getMessage();
            Assert.assertEquals(messageFromDB, message);

        }catch (Exception e){
            LOGGER.info("error ");
        }
    }
}
