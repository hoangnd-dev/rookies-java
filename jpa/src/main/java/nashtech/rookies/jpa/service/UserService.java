package nashtech.rookies.jpa.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import nashtech.rookies.jpa.entity.DepartmentEntity;
import nashtech.rookies.jpa.entity.RoleEntity;
import nashtech.rookies.jpa.entity.UserEntity;
import nashtech.rookies.jpa.entity.UserProfileEntity;

@org.springframework.stereotype.Service
public interface UserService extends Service<UserEntity, UUID> {

    Optional<UserEntity> findUserByUsername(String username);

    List<UserEntity> findOneByName(String name);

    List<UserEntity> findOneByNameQuery(String name);

    List<UserEntity> findOneByNameSpecification(String name);

    List<UserEntity> findOneByNameExample(String name);

    UserProfileEntity save(UserProfileEntity userProfileEntity);

    DepartmentEntity save(DepartmentEntity departmentEntity);

    UserEntity addProfile(UUID userId, UserProfileEntity userProfileEntity);

    List<RoleEntity> findRoleByCodes (List<String> roleCode);

    UserEntity getUser(UUID userId);
}
