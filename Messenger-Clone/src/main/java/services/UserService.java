package services;

import entities.Forum;
import entities.Group;
import entities.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class UserService {

    @Inject
    @PersistenceContext
    private EntityManager em;

    @Inject
    private GroupService groupService;

    @Transactional
    public void saveUser(User user) {
        em.persist(user);
    }

    @Transactional
    public User updateUser(User user) {
        return em.merge(user);
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
    public void deleteUser(Long id) {
        // Find the forum by id
        User user = findUserById(id);

        // If the forum is found
        if (user != null) {
            // Load the groups associated with the forum and set their forum to null
            user.getGroups().forEach(group -> {
                group.getUsers().remove(user);
                groupService.update(group); // Save the group with the now null forum
            });

            // Now delete the forum
            em.remove(user);
        }
    }

    @Transactional
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

}
