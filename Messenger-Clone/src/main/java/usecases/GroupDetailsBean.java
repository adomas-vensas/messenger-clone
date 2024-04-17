package usecases;

import entities.Group;
import entities.User;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import services.GroupService;
import services.UserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named
@ViewScoped
public class GroupDetailsBean implements Serializable {

    @Getter @Setter
    private Set<User> previousUsers;

    @Getter @Setter
    private Group group = new Group();

    @Getter
    private List<User> users;

    @Getter @Setter
    private List<User> selectedUsers= new ArrayList<>();

    @Inject
    private GroupService groupService;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String id = context.getExternalContext().getRequestParameterMap().get("groupId");

        if(id == null)
        {
            return;
        }

        Long numUserId = Long.valueOf(id);
        group = groupService.findGroupById(numUserId);

        loadAllData(group);
    }

    @Transactional
    public String updateGroup() {
        Set<User> managedUsers = new HashSet<>();

        // Remove the group from users who are no longer selected
        for (User previousUser : previousUsers) {
            if (!selectedUsers.contains(previousUser)) {
                User managedUser = userService.updateUser(previousUser);
                managedUser.getGroups().removeIf(groupInSet -> groupInSet.getId().equals(group.getId()));
                userService.updateUser(managedUser);
            }
        }

        for (User selectedUser : selectedUsers) {
            User managedUser = userService.updateUser(selectedUser);
            if (!managedUser.getGroups().contains(group)) {
                managedUser.getGroups().add(group);
                userService.updateUser(managedUser);
            }
            managedUsers.add(managedUser);
        }

        // Update the group's set of users
        group.setUsers(managedUsers);
        groupService.updateGroup(group);

        return "/index?faces-redirect=true";
    }

    @Transactional
    public String deleteGroup()
    {
        groupService.deleteGroup(group.getId());

        return "/index?faces-redirect=true";
    }


    public void loadAllData(Group group)
    {
        previousUsers = group.getUsers();
        selectedUsers = new ArrayList<>(group.getUsers());

        users = userService.getAllUsers();
    }

}
