package nashtech.rookies.security.service;

import java.util.HashSet;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nashtech.rookies.security.dto.SignUpDto;
import nashtech.rookies.security.entity.User;
import nashtech.rookies.security.exception.UserExistException;
import nashtech.rookies.security.repository.RoleRepository;
import nashtech.rookies.security.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository  roleRepository;

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public UserDetails signUp (SignUpDto data) throws UserExistException {
        var user = userRepository.findOneByUsername(data.username());
        if ( user.isPresent() ) {
            throw new UserExistException("Username already exists");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        var newUser = new User();
        newUser.setPassword(encryptedPassword);
        newUser.setUsername(data.username());
        if ( !data.roles().isEmpty() ) {
            var userRoles = roleRepository.findAllById(data.roles());
            if ( userRoles.size() != data.roles().size() ) {
                throw new UserExistException("Role not found");
            }
            newUser.setRoles(new HashSet<>(userRoles));
        }

        return userRepository.save(newUser);
    }
}
