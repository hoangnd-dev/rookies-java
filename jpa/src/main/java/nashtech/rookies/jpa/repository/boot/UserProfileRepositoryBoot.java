package nashtech.rookies.jpa.repository.boot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nashtech.rookies.jpa.entity.UserProfileEntity;
import nashtech.rookies.jpa.entity.UserProfileId;
import nashtech.rookies.jpa.repository.UserProfileRepository;

@Repository
public interface UserProfileRepositoryBoot
    extends UserProfileRepository, JpaRepository<UserProfileEntity, UserProfileId> {

}
