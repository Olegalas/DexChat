package ua.dexchat.server.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.dexchat.model.TemporaryBuffer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by dexter on 05.04.16.
 */
@Service
@Transactional
public class TemporaryBufferDao {

    private final static Logger LOGGER = Logger.getLogger(TemporaryBufferDao.class);

    @PersistenceContext
    private EntityManager manager;

    public TemporaryBufferDao() {
    }

    public TemporaryBuffer findBuffer(int idOwner){
        return manager.createQuery("SELECT t FROM TemporaryBuffer WHERE t.idOwner = :id", TemporaryBuffer.class)
                .setParameter("id", idOwner).getSingleResult();
    }

    public void refreshBuffer(TemporaryBuffer buff){
        manager.refresh(buff);
    }
}
