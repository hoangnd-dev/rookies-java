package nashtech.rookies.jpa.repository.noboot;

import java.util.function.Supplier;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManager;
import nashtech.rookies.jpa.entity.UserProfileEntity;
import nashtech.rookies.jpa.entity.UserProfileId;
import nashtech.rookies.jpa.repository.UserProfileRepository;

@NoRepositoryBean
public class UserProfileRepositoryNoBoot extends GenericJPA<UserProfileEntity, UserProfileId>
    implements UserProfileRepository {

}
