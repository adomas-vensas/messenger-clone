package usecases;

import entities.Group;
import entities.User;
import interceptors.LogPerformance;
import interfaces.GroupAlternative;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import services.GroupService;
import services.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Named
@RequestScoped
@LogPerformance
public class CreateGroup {

    @Getter @Setter
    private Group group = new Group();

    @Getter
    private List<Group> groups;

    @Getter
    private List<User> users;

    @Getter @Setter
    private List<User> selectedUsers;

    @Inject
    private GroupService groupService;

    @Inject
    private UserService userService;

    @Inject
    private GroupAlternative groupSorter;

    @PostConstruct
    public void init() {
        loadAllData(); // Load groups when bean is initialized
    }

    @Transactional
    public void createGroup()
    {
        for (User user : selectedUsers) {
            user.getGroups().add(group); // This is important for bi-directional relationships
        }

        group.setUsers(new HashSet<User>(selectedUsers));
        groupService.save(group);
        groups = groupService.getAll();
    }

    public void loadAllData()
    {
        groups = groupService.getAll();
        users = userService.getAllUsers();
    }

    public void sortGroups(){
        this.groups = groupSorter.groupAlternate(groups);
    }

}
