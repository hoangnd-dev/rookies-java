package nashtech.rookies.jpa.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "USERS")
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
@SecondaryTable(name = "USERS_EXT", pkJoinColumns = @PrimaryKeyJoinColumn(name = "ID"))
public class UserEntity extends AuditEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "ID")
    private UUID id;

    @Column(name = "USER_NAME", length = 36, updatable = false, unique = true)
    private String userName;

    @Column(name = "PASSWORD", length = 1024, nullable = false)
    @ToString.Exclude
    private String password;
    @Column(name = "EMAIL", length = 1024, nullable = false, unique = true)
    //    Validator
    @Email
    private String email;

    @Column(name = "LAST_NAME", length = 100)
    private String lastName;
    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;

    @Builder.Default
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "DISABLED", length = 3)
    private Boolean disabled = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<UserProfileEntity> profiles;


    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntity> roles;

    @Column(name = "USER_AVATAR", table = "USERS_EXT")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @ToString.Exclude
    byte[] avatar;


    @Version
    @Builder.Default
    long version = 1;


    public byte[] getAvatar () {
        if ( this.avatar != null && this.avatar.length > 0 ) {
            return Arrays.copyOf(this.avatar, this.avatar.length);
        }
        return new byte[0];
    }

    public void setAvatar (byte[] data) {
        this.avatar = Arrays.copyOf(data, data.length);
    }

    // Transient
    @Transient
    public String getName () {
        return String.format("%s %s", this.firstName, this.lastName);
    }

}
