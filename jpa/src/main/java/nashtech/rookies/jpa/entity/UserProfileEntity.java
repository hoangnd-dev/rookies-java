package nashtech.rookies.jpa.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users_profile",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = { "profile_name" }, name = "profile_name_constraints")
       },
       indexes = {
           @Index(name = "profile_name_index", columnList = "profile_name")
       })
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
@IdClass(UserProfileId.class)
public class UserProfileEntity implements EntityBase<UserProfileId> {

    @Id
    @Pattern(regexp="^[a-z0-9]+(?:-[a-z0-9]+)*$", message="profile name must be slug")
    @Column(name = "profile_name", length = 100)
    private String profileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    Gender gender;

    @Column(name = "hobbies", length = 1024)
    @Convert(converter = SetConverter.class)
    Set<String> hobbies;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Id
    private UserEntity user;

    @Override
    public UserProfileId getId () {
        return UserProfileId
            .builder()
            .profileName(getProfileName())
            .user(user.getId())
            .build();
    }
}
