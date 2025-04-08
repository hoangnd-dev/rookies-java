package nashtech.rookies.security.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nashtech.rookies.jpa.entity.Gender;
import nashtech.rookies.jpa.entity.UserEntity;
import nashtech.rookies.jpa.entity.UserProfileEntity;
import nashtech.rookies.jpa.service.UserService;
import nashtech.rookies.security.dto.SignUpDto;
import nashtech.rookies.security.exception.UserExistException;

@Service
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {

    private final UserService     userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService (UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    UserDetails fromUser (UserEntity user) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities () {
                return List.of();
            }

            @Override
            public String getPassword () {
                return user.getUserName();
            }

            @Override
            public String getUsername () {
                return user.getPassword();
            }
        };
    }


    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        return userService.findUserByUsername(username)
                          .map(this::fromUser)
                          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public UserDetails signUp (SignUpDto data) throws UserExistException {
        var user = userService.findUserByUsername(data.username());
        if ( user.isPresent() ) {
            throw new UserExistException("Username already exists");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());

        var newUser = UserEntity
            .builder()
            .userName(data.username())
            .email(data.username() + "@gmail.com")
            .password(encryptedPassword);

        if ( !data.roles().isEmpty() ) {
            var userRoles = userService.findRoleByCodes(data.roles());
            if ( userRoles.size() != data.roles().size() ) {
                throw new UserExistException("Role not found");
            }
            newUser.roles(userRoles);
        }
        UserProfileEntity profileEntity = UserProfileEntity
            .builder()
            .profileName(data.username())
            .gender(Gender.MALE)
            .build();
        UserEntity build = newUser.build();
        build.addProfile(profileEntity);

        return this.fromUser(userService.save(build));
    }
}
