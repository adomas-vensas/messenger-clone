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
public class CreateUser {

    @Getter @Setter
    private User user = new User();

    @Getter
    private List<Group> groups;

    @Getter
    private List<User> users;

    @Getter @Setter
    private List<Group> selectedGroups;

    @Inject
    private GroupService groupService;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        loadAllData(); // Load groups when bean is initialized
    }

    @Transactional
    public void createUser()
    {
        for (Group group : selectedGroups) {
            user.getGroups().add(group); // This is important for bi-directional relationships
        }

        user.setGroups(new HashSet<Group>(selectedGroups));
        userService.saveUser(user);
        users = userService.getAllUsers();
    }

    public void loadAllData()
    {
        groups = groupService.getAll();
        users = userService.getAllUsers();
    }

}
