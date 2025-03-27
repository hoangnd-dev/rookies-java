package nashtech.rookies.jpa.repository.noboot;

import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.RoleEntity;
import nashtech.rookies.jpa.repository.RoleRepository;

@NoRepositoryBean
public class RoleRepositoryNoBoot extends GenericJPA<RoleEntity, String> implements RoleRepository {

}
