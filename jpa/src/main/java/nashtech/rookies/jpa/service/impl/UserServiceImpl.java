package nashtech.rookies.jpa.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nashtech.rookies.jpa.entity.DepartmentEntity;
import nashtech.rookies.jpa.entity.RoleEntity;
import nashtech.rookies.jpa.entity.UserEntity;
import nashtech.rookies.jpa.entity.UserProfileEntity;
import nashtech.rookies.jpa.repository.DepartmentRepository;
import nashtech.rookies.jpa.repository.RoleRepository;
import nashtech.rookies.jpa.repository.UserProfileRepository;
import nashtech.rookies.jpa.repository.UserRepository;
import nashtech.rookies.jpa.repository.boot.UserRepositoryBoot;
import nashtech.rookies.jpa.service.UserService;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends ServiceImpl<UserEntity, UUID> implements UserService {

    private final UserRepository        userRepository;
    private final RoleRepository        roleRepository;
    private final PasswordEncoder       passwordEncoder;
    private final UserProfileRepository userProfileRepository;
    private final DepartmentRepository  departmentRepository;

    @Inject
    public UserServiceImpl (UserRepository userRepository, RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder, UserProfileRepository userProfileRepository,
                            DepartmentRepository departmentRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userProfileRepository = userProfileRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Optional<UserEntity> findUserByUsername (String username) {
        return this.userRepository.findOneByUserName(username);
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

    @Override
    @Transactional
    public DepartmentEntity save (DepartmentEntity departmentEntity) {
        return departmentRepository.save(departmentEntity);

    }

    @Override
    @Transactional
    public UserEntity addProfile (UUID userId, UserProfileEntity userProfileEntity) {
        return this.findOne(userId)
                   .map(u -> {
                       u.addProfile(userProfileEntity);
                       return this.save(u);
                   }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<RoleEntity> findRoleByCodes (List<String> roleCode) {
        return this.roleRepository.findAllByCodeIn(roleCode);
    }

    @Override
    public UserEntity getUser (UUID userId) {
        System.out.println("GET USER");
        Optional<UserEntity> byId = this.userRepository.findById(userId);
        var u = byId.get();
        System.out.println("Print department");
        System.out.println(u.getDepartment().getName());
        return u;
    }


}
