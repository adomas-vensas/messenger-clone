package DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ForumCreationRequest {
    private String name;
    private List<Long> groupIds;
}
