package services;

import entities.Forum;
import entities.Group;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class ForumService {

    @Inject
    private EntityManager em;

    @Transactional
    public void saveForum(Forum message) {
        em.persist(message);
    }

    @Transactional
    public Forum findForumById(Long id) {
        return em.find(Forum.class, id);
    }

    @Transactional
    public Forum findForumsByIds(List<Long> ids)
    {
        return em.find(Forum.class, ids);
    }

    @Transactional
    public void deleteForum(Long id) {
        Forum message = em.find(Forum.class, id);
        if (message != null) {
            em.remove(message);
        }
    }

    public List<Forum> getAllForums() {
        return em.createQuery("SELECT f FROM Forum f", Forum.class).getResultList();
    }

    public void updateForum(Forum forum)
    {
        em.merge(forum);
    }

}
