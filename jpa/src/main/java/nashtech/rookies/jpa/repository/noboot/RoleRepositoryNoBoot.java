package nashtech.rookies.jpa.repository.noboot;

import java.util.function.Supplier;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManager;
import nashtech.rookies.jpa.entity.RoleEntity;
import nashtech.rookies.jpa.repository.RoleRepository;

@NoRepositoryBean
public class RoleRepositoryNoBoot extends GenericJPA<RoleEntity, String> implements RoleRepository {

}
