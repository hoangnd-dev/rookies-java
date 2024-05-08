package nashtech.rookies.security.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NamedEntityGraph(name = User.WITH_RULES_GRAPH,
    attributeNodes = @NamedAttributeNode("roles"))
@Table(name = "USERS")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails {

    public static final String WITH_RULES_GRAPH = "graph.User.roles";

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    Long id;

    @Column(unique = true, length = 100)
    String username;

    @Column(length = 2500)
    String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
        name = "USERS_ROLES",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        if ( this.roles != null ) {
            return this.roles.stream().map(e -> new SimpleGrantedAuthority(e.getRoleName())).toList();

        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired () {
        return true;
    }

    @Override
    public boolean isAccountNonLocked () {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired () {
        return true;
    }

    @Override
    public boolean isEnabled () {
        return true;
    }

}
