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

    @Inject
    private GroupService groupService;

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
    public void deleteForum(Long forumId) {
        // Find the forum by id
        Forum forum = findForumById(forumId);

        // If the forum is found
        if (forum != null) {
            // Load the groups associated with the forum and set their forum to null
            forum.getGroups().forEach(group -> {
                group.setForum(null);
                groupService.update(group); // Save the group with the now null forum
            });

            // Now delete the forum
            em.remove(forum);
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
