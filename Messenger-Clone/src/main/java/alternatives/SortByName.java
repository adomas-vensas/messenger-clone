package alternatives;

import entities.Group;
import interfaces.GroupAlternative;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Alternative
public class SortByName implements GroupAlternative {

    @Override
    public List<Group> groupAlternate(List<Group> allGroups)
    {
        return allGroups.stream()
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());
    }

}
