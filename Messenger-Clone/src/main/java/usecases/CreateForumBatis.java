package usecases;

import entities.Forum;
import entities.Group;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import mappers.ForumMapper;
import services.ForumService;
import services.GroupService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named
@RequestScoped
public class CreateForumBatis {

    @Inject
    private ForumMapper forumMapper;

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

        for (Group selectedGroup : selectedGroups) {
            Group managedGroup = groupService.updateGroup(selectedGroup);
            managedGroup.setForum(forum);
            managedGroups.add(managedGroup);
        }

        forum.setGroups(managedGroups);
        forumMapper.insertForum(forum);

        loadAllData();
    }

    public void loadAllData()
    {
        groups = groupService.getUnavailableGroups();
        forums = forumMapper.getAllForums();
    }
}
