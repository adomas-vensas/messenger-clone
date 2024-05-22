package DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ForumDTO {
    private Long id;
    private String name;
    private List<Long> groupIds = new ArrayList<>();
}
