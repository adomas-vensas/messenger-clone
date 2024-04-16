package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "GROUPS")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TEXT")
    private String text;


    @ManyToMany(mappedBy = "groups")
    private Set<User> users;


}
