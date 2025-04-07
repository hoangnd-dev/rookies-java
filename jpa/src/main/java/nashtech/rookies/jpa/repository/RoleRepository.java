package nashtech.rookies.jpa.repository;

import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.RoleEntity;

@NoRepositoryBean
public interface RoleRepository extends Repository<RoleEntity, String> {
}
