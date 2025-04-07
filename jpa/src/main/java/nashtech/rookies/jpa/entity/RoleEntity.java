package nashtech.rookies.jpa.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@Table(name = "roles")
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleEntity  extends AuditEntity<String> {

    @Id
    @Column(name = "role_code")
    String id;

    @Column(name = "role_name")
    String name;

    //@Column(name = "permissions")
    //@ElementCollection(fetch = FetchType.EAGER)
    //@JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_code", columnDefinition = "bytea"))
    //private List<Permission> positions;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<UserEntity> users;

}
