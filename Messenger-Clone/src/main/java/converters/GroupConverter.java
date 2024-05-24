package converters;

import entities.Group;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import services.GroupService;

@FacesConverter(forClass = Group.class)
public class GroupConverter implements Converter<Group> {

    @Inject
    private GroupService groupService;

    @Override
    public Group getAsObject(FacesContext context, UIComponent component, String value) {
        // Implementation to convert the unique String identifier back to a Group object
        return groupService.findById(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Group value) {
        // Convert the Group object to a unique String identifier, such as the primary key
        return (value != null) ? String.valueOf(value.getId()) : null;
    }
}
