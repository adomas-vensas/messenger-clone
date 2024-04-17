package services;

import entities.Forum;
import entities.Group;
import entities.Group;
import entities.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class GroupService {

    @Inject
    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserService userService;

    @Transactional
    public void saveGroup(Group group) {
        em.persist(group);
    }

    @Transactional
    public Group findGroupById(Long id) {
        return em.find(Group.class, id);
    }

    @Transactional
    public Group findGroupsByIds(List<Long> ids)
    {
        return em.find(Group.class, ids);
    }

    @Transactional
    public void deleteGroup(Long id) {
        Group group = findGroupById(id);

        if (group != null) {
            group.getUsers().forEach(user -> {
                user.getGroups().remove(group);
                userService.updateUser(user);
            });

            em.remove(group);
        }
    }

    @Transactional
    public List<Group> getAllGroups() {
        return em.createQuery("SELECT g FROM Group g", Group.class).getResultList();
    }

    @Transactional
    public Group updateGroup(Group group)
    {
        return em.merge(group);
    }

    @Transactional
    public List<Group> getAvailableGroups(Forum forum)
    {
        return em.createQuery(
                "SELECT g FROM Group g WHERE g.forum = :forum", Group.class)
                .setParameter("forum", forum)
                .getResultList();
    }


    @Transactional
    public List<Group> getUnavailableGroups()
    {
        return em.createQuery("SELECT g FROM Group g WHERE g.forum.id IS NULL ", Group.class).getResultList();
    }
}
