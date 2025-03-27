package nashtech.rookies.jpa.repository.noboot;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.NoRepositoryBean;

import jakarta.persistence.EntityGraph;
import nashtech.rookies.jpa.entity.UserEntity;
import nashtech.rookies.jpa.repository.UserRepository;

@NoRepositoryBean
public class UserRepositoryNoBoot extends GenericJPA<UserEntity, UUID> implements UserRepository {

    @Override
    public List<UserEntity> findAllByUserNameLikeIgnoreCaseOrderById (String username) {
        return List.of();
    }

    @Override
    public List<UserEntity> getAllUserByQuery (String name) {
        return List.of();
    }

    @Override
    public Optional<UserEntity> getUserWithDepartmentByUsernameJoinFetch (String username) {
        return Optional.ofNullable(
            doInTransaction(entityManager -> entityManager
                .createQuery(
                    "SELECT u FROM UserEntity u JOIN FETCH u.department WHERE u.userName = :userName",
                    UserEntity.class
                )
                .setParameter("userName", username)
                .getSingleResult()));
    }

    @Override
    public Optional<UserEntity> getUserWithDepartmentByUsernameGraph (String username) {
        return Optional.ofNullable(doInTransaction(entityManager -> {
            EntityGraph<UserEntity> graph = entityManager.createEntityGraph(UserEntity.class);
            graph.addAttributeNodes("department");
            return entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.userName = :userName", UserEntity.class)
                                .setParameter("userName", username)
                                .setHint("jakarta.persistence.loadgraph", graph)
                                .getSingleResult();
        }));
    }

    @Override
    public Optional<UserEntity> getUserWithAvatarByUsernameJoinFetch (String username) {
        return Optional.ofNullable(doInTransaction(entityManager -> {
            EntityGraph<UserEntity> graph = entityManager.createEntityGraph(UserEntity.class);
            graph.addAttributeNodes("avatar");
            return entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.userName = :userName", UserEntity.class)
                                .setParameter("userName", username)
                                .setHint("jakarta.persistence.loadgraph", graph)
                                .getSingleResult();
        }));
    }

}
