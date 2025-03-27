package nashtech.rookies.jpa.repository.noboot;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.NoRepositoryBean;

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

}
