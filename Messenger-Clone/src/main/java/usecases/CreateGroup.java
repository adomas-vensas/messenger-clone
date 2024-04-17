package usecases;

import entities.Group;
import entities.User;
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
        groupService.saveGroup(group);
        groups = groupService.getAllGroups();
    }

    public void loadAllData()
    {
        groups = groupService.getAllGroups();
        users = userService.getAllUsers();
    }

}
