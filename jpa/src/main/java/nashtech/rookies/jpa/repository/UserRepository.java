package nashtech.rookies.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.UserEntity;

@NoRepositoryBean
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    List<UserEntity> findAllByUserNameLikeIgnoreCaseOrderById (String userName);

    List<UserEntity> getAllUserByQuery (String userName);

    Optional<UserEntity> getUserWithDepartmentByUsernameJoinFetch (String username);

    Optional<UserEntity> getUserWithDepartmentByUsernameGraph (String username);

    Optional<UserEntity> getUserWithAvatarByUsernameJoinFetch (String username);
}
