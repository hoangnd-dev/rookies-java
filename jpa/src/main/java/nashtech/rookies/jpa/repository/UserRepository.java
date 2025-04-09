package nashtech.rookies.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.UserEntity;

@NoRepositoryBean
public interface UserRepository extends Repository<UserEntity, UUID> {

    List<UserEntity> findAllByUserNameLikeIgnoreCaseOrderById (String userName);

    List<UserEntity> getAllUserByQuery (String userName);

    UserEntity getUserWithDepartmentByUsernameJoinFetch (String username);

    @EntityGraph(attributePaths = {"roles", "department"})
    Optional<UserEntity> findOneByUserName (String username);

}
