package nashtech.rookies.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import nashtech.rookies.security.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<UserDetails> findUserByUsername (String username);

    Optional<User> findOneByUsername(String username);

}
