package ua.dexchat.server.dao;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.dexchat.model.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by dexter on 03.04.16.
 */
@Service
@Transactional
public class ClientDao {

    private final static Logger LOGGER = Logger.getLogger(ClientDao.class);

    @PersistenceContext
    private EntityManager manager;


    public ClientDao() {
    }

    public Client findClient(Login login){

        try{
            Client fromResult = manager.createQuery("SELECT c FROM Client c WHERE c.login = :login", Client.class)
                    .setParameter("login", login.login).getSingleResult();
            LOGGER.info("***Client was founded");
            LOGGER.info(fromResult);

            return login.pass.equals(fromResult.getPass()) ? fromResult : null;
        } catch (Exception e){

            LOGGER.error("***Something wrong :", e);

            return null;
        }
    }

    public Client findClient(int id){
        return manager.find(Client.class, id);
    }

    public void saveClient(Login login){
        Client client = new Client(login);
        saveClient(client);
    }

    public void saveClient(Client client){
        manager.persist(client);
    }

    public List<Client> findClientsByLogin(String login, int amount){
        List<Client> clients = manager.createQuery("SELECT c FROM Client c WHERE c.login LIKE :login", Client.class)
                .setParameter("login", login + "%").setMaxResults(amount).getResultList();

        return clients;
    }

    public Client findClientByLogin(String login) {
        Client fromResult = manager.createQuery("SELECT c FROM Client c WHERE c.login = :login", Client.class)
                .setParameter("login", login).getSingleResult();

        return fromResult;
    }

    public void addFriendToClient(String clientLogin, String friendLogin){
        Client client = manager.createQuery("SELECT c FROM Client c WHERE c.login = :login", Client.class)
                .setParameter("login", clientLogin).getSingleResult();
        Client friend = manager.createQuery("SELECT c FROM Client c WHERE c.login = :login", Client.class)
                .setParameter("login", friendLogin).getSingleResult();

        client.getMyFriends().add(friend);
        friend.getiFriendTo().add(client);

        manager.merge(client);
        manager.merge(friend);
    }

    public void removeFriendFromClient(String clientLogin, String friendLogin){
        Client client = manager.createQuery("SELECT c FROM Client c WHERE c.login = :login", Client.class)
                .setParameter("login", clientLogin).getSingleResult();
        Client friend = manager.createQuery("SELECT c FROM Client c WHERE c.login = :login", Client.class)
                .setParameter("login", friendLogin).getSingleResult();

        client.getMyFriends().remove(friend);
        friend.getiFriendTo().remove(client);

        manager.merge(client);
        manager.merge(friend);
    }

}
