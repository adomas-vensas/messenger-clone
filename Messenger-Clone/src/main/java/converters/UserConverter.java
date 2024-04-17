package converters;

import entities.User;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import services.UserService;

@FacesConverter(forClass = User.class)
public class UserConverter implements Converter<User> {

    @Inject
    private UserService userService;

    @Override
    public User getAsObject(FacesContext context, UIComponent component, String value) {
        // Implementation to convert the unique String identifier back to a User object
        return userService.findUserById(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, User value) {
        // Convert the User object to a unique String identifier, such as the primary key
        return (value != null) ? String.valueOf(value.getId()) : null;
    }
}
