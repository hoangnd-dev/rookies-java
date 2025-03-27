package nashtech.rookies.jpa.service;

import java.util.List;
import java.util.UUID;

import nashtech.rookies.jpa.entity.UserEntity;

@org.springframework.stereotype.Service
public interface UserService extends Service<UserEntity, UUID> {

    List<UserEntity> findOneByName(String name);

    List<UserEntity> findOneByNameQuery(String name);

    List<UserEntity> findOneByNameSpecification(String name);

    List<UserEntity> findOneByNameExample(String name);
}
