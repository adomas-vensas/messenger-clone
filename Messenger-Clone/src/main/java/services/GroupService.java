package services;

import entities.Group;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class GroupService {

    @Inject
    private EntityManager em;

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
    public void deleteMessage(Long id) {
        Group group = em.find(Group.class, id);
        if (group != null) {
            em.remove(group);
        }
    }

    public GroupService(EntityManager em) {
        this.em = em;
    }
}
