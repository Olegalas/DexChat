package ua.dexchat.server.dao;

import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.dexchat.model.Client;
import ua.dexchat.model.Login;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
            Client fromResult = manager.createQuery("SELECT c FROM ua.dexchat.model.Client c WHERE c.login = :login", Client.class)
                    .setParameter("login", login.login).getSingleResult();
            LOGGER.info("***Client was founded");
            LOGGER.info(fromResult);

            return login.pass.equals(fromResult.getPass()) ? fromResult : null;
        } catch (NoResultException e){

            LOGGER.info("***Clients table is empty");

            return null;
        } catch (DataIntegrityViolationException e){

            LOGGER.info("***Clients table is empty");

            return null;
        }
    }

    public int saveClient(Login login){

        Client client = new Client(login);

        try{
            manager.persist(client);
        }catch (Exception e){
            LOGGER.error("***This login has already used: " + e.getMessage());
            return -1;
        }
        LOGGER.info("***Client almost saved");
        return client.getId();
    }
}