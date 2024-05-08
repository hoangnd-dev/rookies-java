package nashtech.rookies.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nashtech.rookies.security.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

}
