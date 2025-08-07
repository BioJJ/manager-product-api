package biojj.managerproductapi.domain.model;

import biojj.managerproductapi.domain.enums.Profile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private Boolean status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_profiles")
    @Column(name = "profile_id")
    private Set<Integer> profiles;

    private LocalDate dataCreation;

    public Set<Profile> getProfilesAsEnum() {
        return profiles.stream()
                .map(Profile::fromCode)
                .collect(Collectors.toSet());
    }
}