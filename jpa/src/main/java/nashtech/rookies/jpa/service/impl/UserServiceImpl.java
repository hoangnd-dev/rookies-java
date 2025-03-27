package nashtech.rookies.jpa.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nashtech.rookies.jpa.entity.UserEntity;
import nashtech.rookies.jpa.entity.UserProfileEntity;
import nashtech.rookies.jpa.repository.RoleRepository;
import nashtech.rookies.jpa.repository.UserProfileRepository;
import nashtech.rookies.jpa.repository.UserRepository;
import nashtech.rookies.jpa.repository.boot.UserRepositoryBoot;
import nashtech.rookies.jpa.service.UserService;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends ServiceImpl<UserEntity, UUID> implements UserService {

    private final UserRepository  userRepository;
    private final RoleRepository  roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository userProfileRepository;

    public UserServiceImpl (UserRepository userRepository, RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder, UserProfileRepository userProfileRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public List<UserEntity> findOneByName (String name) {
        return userRepository.findAllByUserNameLikeIgnoreCaseOrderById(name);
    }

    @Override
    public List<UserEntity> findOneByNameQuery (String name) {
        return this.userRepository.getAllUserByQuery(name);
    }

    @Override
    public List<UserEntity> findOneByNameSpecification (String name) {
        Specification<UserEntity> specification = (root, query, criteriaBuilder) -> {
            var namePredicate = criteriaBuilder.like(root.get("name"), name);
            return criteriaBuilder.and(namePredicate);
        };

        if ( userRepository instanceof UserRepositoryBoot userRepositoryBoot ) {
            return userRepositoryBoot.findAll(specification);
        }
        return Collections.emptyList();
    }

    @Override
    public List<UserEntity> findOneByNameExample (String name) {
        var userEntity = UserEntity
            .builder()
            .userName(name)
            .build();
        var exampleMatcher = ExampleMatcher.matching()
                                           .withIgnoreCase()
                                           .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        var example = Example.of(userEntity, exampleMatcher);
        if ( userRepository instanceof UserRepositoryBoot userRepositoryBoot ) {
            return userRepositoryBoot.findAll(example);
        }
        return Collections.emptyList();
    }

    @Override
    public UserProfileEntity save (UserProfileEntity userProfileEntity) {
        return this.userProfileRepository.save(userProfileEntity);
    }

}
