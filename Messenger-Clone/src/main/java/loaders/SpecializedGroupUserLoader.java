package loaders;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Specializes;

import java.util.concurrent.CompletableFuture;

@ApplicationScoped
@Specializes
public class SpecializedGroupUserLoader extends GroupUserLoader{
    public void loadGroupUsers(Long genreId)
    {
        getGroupsWithLoadedUsers().put(genreId, CompletableFuture.runAsync(this::loadUsersAsync));
    }

    private void loadUsersAsync(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }
}
