package services;

import entities.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class UserService {

    @Inject
    private EntityManager em;

    @Transactional
    public void saveUser(User user) {
        em.persist(user);
    }

    @Transactional
    public User findUserById(Long id) {
        return em.find(User.class, id);
    }

    @Transactional
    public User findUsersByIds(List<Long> ids)
    {
        return em.find(User.class, ids);
    }

    @Transactional
    public void deleteMessage(Long id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
    }

    public UserService(EntityManager em) {
        this.em = em;
    }
}
