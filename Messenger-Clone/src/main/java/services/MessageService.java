package services;

import entities.Message;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class MessageService {

    @Inject
    private EntityManager em;

    @Transactional
    public void saveMessage(Message message) {
        em.persist(message);
    }

    @Transactional
    public Message findMessageById(Long id) {
        return em.find(Message.class, id);
    }

    @Transactional
    public Message findMessagesByIds(List<Long> ids)
    {
        return em.find(Message.class, ids);
    }

    @Transactional
    public void deleteMessage(Long id) {
        Message message = em.find(Message.class, id);
        if (message != null) {
            em.remove(message);
        }
    }

    public MessageService(EntityManager em) {
        this.em = em;
    }
}
