package nashtech.rookies.jpa.repository.boot;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nashtech.rookies.jpa.entity.UserEntity;
import nashtech.rookies.jpa.repository.UserRepository;

@Repository
public interface UserRepositoryBoot
    extends UserRepository, JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

    List<UserEntity> findAllByUserNameLikeIgnoreCaseOrderById (String name);

    @Query("SELECT b from UserEntity b where lower(b.userName) like lower(:userName) order by b.id")
    List<UserEntity> getAllUserByQuery (String name);
}
