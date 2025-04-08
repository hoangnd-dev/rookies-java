package nashtech.rookies.jpa.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.hibernate.proxy.HibernateProxy;

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
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@SecondaryTable(name = "users_ext", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
@NamedEntityGraphs(
    value = {
        @NamedEntityGraph(name = UserEntity.WITH_RULES_GRAPH,
                          attributeNodes = @NamedAttributeNode("roles")),
        @NamedEntityGraph(name = UserEntity.WITH_RULES_PROFILE,
                          attributeNodes = @NamedAttributeNode("profiles"))
    }
)
public class UserEntity extends AuditEntity<UUID> {

    public static final String WITH_RULES_GRAPH   = "graph.User.roles";
    public static final String WITH_RULES_PROFILE = "graph.User.profiles";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private UUID id;



    @Column(name = "user_name", updatable = false, unique = true)
    private String userName;

    @Column(name = "password", length = 1024, nullable = false)
    @ToString.Exclude
    private String password;
    @Column(name = "email", length = 1024, nullable = false, unique = true)
    //    Validator
    @Email
    private String email;

    @Column(name = "last_name", length = 100)
    private String lastName;
    @Column(name = "first_name", length = 100)
    private String firstName;


    @Builder.Default
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "disabled", length = 3)
    private Boolean disabled = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @NotEmpty
    @Builder.Default
    private Set<UserProfileEntity> profiles = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_code", referencedColumnName = "role_code"))
    private List<RoleEntity> roles;

    @Column(name = "user_avatar", table = "users_ext")
    //@Lob
    //@Basic(fetch = FetchType.LAZY)
    @ToString.Exclude
    String avatar;


    @Version
    @Builder.Default
    long version = 1;



    // Transient
    @Transient
    public String getName () {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    @Transient
    public void addProfile(UserProfileEntity profile) {
        profile.setUser(this);
        this.profiles.add(profile);
    }

    @Override
    public final boolean equals (Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null ) {
            return false;
        }
        Class<?> oEffectiveClass =
            o instanceof HibernateProxy ? ((HibernateProxy) o)
                .getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass =
            this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if ( thisEffectiveClass != oEffectiveClass ) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode () {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                                                                       .getPersistentClass()
                                                                       .hashCode() : getClass().hashCode();
    }
}
