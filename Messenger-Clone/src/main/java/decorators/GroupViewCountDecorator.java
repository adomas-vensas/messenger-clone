package decorators;

import entities.Group;
import interfaces.GenericService;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import services.GroupService;

@Decorator
@Dependent
public abstract class GroupViewCountDecorator implements GenericService<Group> {
    @Inject
    @Delegate
    private GroupService groupService;

    @Override
    @Transactional
    public Group findById(Long id){
        Group group = groupService.findById(id);

        int viewCount = group.getViewCount() == null ? 0 : group.getViewCount() + 1;
        group.setViewCount(viewCount);
        groupService.update(group);
        return group;
    }
}
