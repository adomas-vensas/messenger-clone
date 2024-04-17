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
public class UserDetailsBean implements Serializable {

    @Getter @Setter
    private Set<Group> previousGroups;

    @Getter @Setter
    private User user = new User();

    @Getter
    private List<Group> groups;

    @Getter @Setter
    private List<Group> selectedGroups = new ArrayList<>();

    @Inject
    private GroupService groupService;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String id = context.getExternalContext().getRequestParameterMap().get("userId");

        if(id == null)
        {
            return;
        }

        Long numUserId = Long.valueOf(id);
        user = userService.findUserById(numUserId);

        loadAllData(user);
    }

    @Transactional
    public String updateUser()
    {
        Set<Group> managedGroups = new HashSet<>();

        for (Group previousGroup : previousGroups) {
            Group managedGroup = groupService.updateGroup(previousGroup);
            managedGroup.getUsers().remove(user);
        }

        for (Group selectedGroup : selectedGroups) {
            Group managedGroup = groupService.updateGroup(selectedGroup);
            managedGroup.getUsers().add(user);
            managedGroups.add(managedGroup);
        }

        user.setGroups(managedGroups);
        userService.updateUser(user);

        return "/user_page?faces-redirect=true";
    }

    @Transactional
    public String deleteUser()
    {
        userService.deleteUser(user.getId());

        return "/user_page?faces-redirect=true";
    }


    public void loadAllData(User user)
    {
        previousGroups = user.getGroups();
        selectedGroups = new ArrayList<>(user.getGroups());

        groups = groupService.getAllGroups();
    }

}
