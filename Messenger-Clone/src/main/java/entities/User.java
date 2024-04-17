package entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_group", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key for User in user_group table
            inverseJoinColumns = @JoinColumn(name = "group_id") // Foreign key for Group in user_group table
    )
    private Set<Group> groups = new HashSet<Group>();

}
