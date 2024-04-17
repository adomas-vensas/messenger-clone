package usecases;

import entities.Forum;
import entities.Group;
import entities.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import services.ForumService;
import services.GroupService;
import services.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named
@RequestScoped
public class CreateForum {

    @Getter @Setter
    private Forum forum = new Forum();

    @Getter
    private List<Group> groups;

    @Getter
    private List<Forum> forums;

    @Getter @Setter
    private List<Group> selectedGroups;

    @Inject
    private GroupService groupService;

    @Inject
    private ForumService forumService;

    @PostConstruct
    public void init() {
        loadAllData(); // Load groups when bean is initialized
    }

    @Transactional
    public void createForum()
    {
        Set<Group> managedGroups = new HashSet<>();

        // For each selected group, merge it into the persistence context
        // and set the forum.
        for (Group selectedGroup : selectedGroups) {
            Group managedGroup = groupService.updateGroup(selectedGroup); // Ensure the group is managed.
            managedGroup.setForum(forum); // Set the forum for each managed group.
            managedGroups.add(managedGroup); // Add the managed group to the set.
        }

        // Set the managed groups to the forum.
        forum.setGroups(managedGroups);
        forumService.saveForum(forum);
        forums = forumService.getAllForums();
    }

    public void loadAllData()
    {
        groups = groupService.getAllGroups();
        forums = forumService.getAllForums();
    }

}
