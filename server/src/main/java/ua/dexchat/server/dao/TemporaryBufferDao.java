package ua.dexchat.server.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.dexchat.model.Message;
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

    public TemporaryBuffer findBufferByIdOwner(int idOwner){
        return manager.createQuery("SELECT t FROM TemporaryBuffer t WHERE t.idOwner = :id", TemporaryBuffer.class)
                .setParameter("id", idOwner).getSingleResult();
    }

    public TemporaryBuffer findBufferById(int id){
        return manager.find(TemporaryBuffer.class, id);
    }

    public void removeBuffer(TemporaryBuffer buff) {
        TemporaryBuffer buffFromFind = manager.find(TemporaryBuffer.class, buff.getId());
        manager.remove(buffFromFind);
        manager.flush();
    }

    public void saveMessageInBuffer(Message message){
        manager.persist(message);
    }

    public void saveTempBuffer(TemporaryBuffer buff){
        manager.persist(buff);
    }
}
