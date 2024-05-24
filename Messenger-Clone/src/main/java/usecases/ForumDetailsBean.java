package usecases;

import entities.Forum;
import entities.Group;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import services.ForumService;
import services.GroupService;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ForumDetailsBean implements Serializable {

    private Set<Group> _previousGroups;

    @Getter @Setter
    private Forum forum;

    @Getter @Setter
    private List<Group> selectedGroups;

    @Getter @Setter
    private List<Group> newSelectedGroups = new ArrayList<>();

    @Getter
    private List<Group> availableGroups;

    @Inject
    private ForumService forumService;

    @Inject
    private GroupService groupService;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String forumId = context.getExternalContext().getRequestParameterMap().get("forumId");

        if(forumId == null)
        {
            return;
        }

        Long numForumId = Long.valueOf(forumId);
        forum = forumService.findForumById(numForumId);

        loadData(forum);
    }

    @Transactional
    public String updateForum() {

        Set<Group> managedGroups = new HashSet<>();

        for (Group previousGroup : _previousGroups) {
            Group managedGroup = groupService.update(previousGroup); // Ensure the group is managed.
            managedGroup.setForum(null); // Set the forum for each managed group.
        }

        for (Group selectedGroup : selectedGroups) {
            Group managedGroup = groupService.update(selectedGroup); // Ensure the group is managed.
            managedGroup.setForum(forum); // Set the forum for each managed group.
            managedGroups.add(managedGroup); // Add the managed group to the set.
        }

        for (Group selectedGroup : newSelectedGroups) {
            Group managedGroup = groupService.update(selectedGroup); // Ensure the group is managed.
            managedGroup.setForum(forum); // Set the forum for each managed group.
            managedGroups.add(managedGroup); // Add the managed group to the set.
        }

        forum.setGroups(managedGroups);
        forumService.updateForum(forum);

        return "/forum_page?faces-redirect=true";
    }

    @Transactional
    public void deleteForum()
    {
        forumService.deleteForum(forum.getId());
        CompletableFuture.runAsync(() ->{
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }

    @Transactional
    private void loadData(Forum forum)
    {
        _previousGroups = forum.getGroups();

        selectedGroups = groupService.getAvailableGroups(forum);
        availableGroups = groupService.getUnavailableGroups();
    }

}
