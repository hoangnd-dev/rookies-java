package nashtech.rookies.jpa.repository;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.RoleEntity;

@NoRepositoryBean
public interface RoleRepository extends Repository<RoleEntity, Long> {

    List<RoleEntity> findAllByCodeIn(List<String> code);
}
