package usecases;

import entities.Forum;
import entities.Group;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import services.ForumService;
import services.GroupService;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named
@ViewScoped
public class ForumDetailsBean implements Serializable {

    @Getter @Setter
    private Forum forum;

    @Getter @Setter
    private List<Group> selectedGroups;

    @Getter
    private List<Group> unavailableGroups;

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

    public String updateForm()
    {
        Set<Group> managedGroups = new HashSet<>();

        for(Group availableGroup : availableGroups)
        {
            Group group = null;

            for(Group selectedGroup : selectedGroups)
            {
                if(selectedGroup.getId().equals(availableGroup.getId()))
                {
                    group = selectedGroup;
                }
            }

            if(group != null)
            {
                Group managedGroup = groupService.updateGroup(group); // Ensure the group is managed.
                managedGroup.setForum(forum); // Set the forum for each managed group.
                managedGroups.add(managedGroup); // Add the managed group to the set.
            }
            else
            {
                Group managedGroup = groupService.updateGroup(availableGroup); // Ensure the group is managed.
                managedGroup.setForum(null); // Set the forum for each managed group.
                managedGroups.add(managedGroup); // Add the managed group to the set.
            }

        }

        for(Group unavailableGroup : unavailableGroups)
        {
            Group group = null;

            for(Group selectedGroup : selectedGroups)
            {
                if(selectedGroup.getId().equals(unavailableGroup.getId()))
                {
                    group = selectedGroup;
                }
            }

            if(group != null)
            {
                Group managedGroup = groupService.updateGroup(group); // Ensure the group is managed.
                managedGroup.setForum(forum); // Set the forum for each managed group.
                managedGroups.add(managedGroup); // Add the managed group to the set.
            }
            else
            {
                Group managedGroup = groupService.updateGroup(unavailableGroup); // Ensure the group is managed.
                managedGroup.setForum(null); // Set the forum for each managed group.
                managedGroups.add(managedGroup); // Add the managed group to the set.
            }

        }

        forum.setGroups(managedGroups);
        forumService.updateForum(forum);

        return "/forum_page?faces-redirect=true";
    }

    private void loadData(Forum forum)
    {
        selectedGroups = groupService.getAvailableGroups(forum);
        availableGroups = groupService.getAvailableGroups(forum);
        unavailableGroups = groupService.getUnavailableGroups(forum);
    }

}
