package nashtech.rookies.jpa.repository;

import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.UserProfileEntity;
import nashtech.rookies.jpa.entity.UserProfileId;

@NoRepositoryBean
public interface UserProfileRepository extends Repository<UserProfileEntity, UserProfileId> {
}
