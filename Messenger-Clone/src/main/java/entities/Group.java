package entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.javassist.util.HotSwapAgent;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "GROUPS")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany(mappedBy = "groups")
    private Set<User> users = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @Basic
    @Column(name = "PAGE_VIEWCOUNT")
    private Integer viewCount = 0;

    @Version
    @Column(name = "OPT_LOCK_VERSION")
    private Integer version;
}
