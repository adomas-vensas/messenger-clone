package loaders;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Model;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Model
@SessionScoped
public class GroupUserLoader implements Serializable {

    @Setter
    @Getter
    private Map<Long, CompletableFuture> groupsWithLoadedUsers = new HashMap<>();

    public void loadGroupUsers(Long genreId)
    {
        groupsWithLoadedUsers.put(genreId, CompletableFuture.runAsync(() -> {}));
    }

}
