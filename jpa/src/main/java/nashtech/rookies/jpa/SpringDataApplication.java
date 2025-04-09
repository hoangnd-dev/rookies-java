package nashtech.rookies.jpa;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.log4j.Log4j2;
import nashtech.rookies.jpa.entity.DepartmentEntity;
import nashtech.rookies.jpa.entity.Gender;
import nashtech.rookies.jpa.entity.UserEntity;
import nashtech.rookies.jpa.entity.UserProfileEntity;
import nashtech.rookies.jpa.service.RoleService;
import nashtech.rookies.jpa.service.UserService;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = "nashtech.rookies.jpa.repository",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ".*NoBoot"
    )
)
@Log4j2
public class SpringDataApplication {

    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate    jdbcTemplate;

    public SpringDataApplication (PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    void showBookRelation (Long id) {
        var authorName = jdbcTemplate.query(
            "SELECT author_id,book_id FROM authors_books where book_id=?",
            (RowMapper<Object>) (rs, rowNum) -> rs.getNString("author_id")
            ,
            id
        );
        log.info("author_id: {}, book_id: {}", authorName, id);
    }

    void showAuthorInfo (Long id) {
        var authorName = jdbcTemplate.query(
            "SELECT author_name FROM authors where id=?",
            (RowMapper<Object>) (rs, rowNum) -> rs.getNString("author_name"),
            id
        );
        log.info("author name: {}", authorName);
        var authorAddress = jdbcTemplate.query(
            "SELECT address FROM authors_detail where author_id=?",
            (RowMapper<Object>) (rs, rowNum) -> rs.getNString("address"),
            id
        );
        log.info("detail address: {}", authorAddress);

    }


    void showAuthorInfo (JdbcTemplate jdbcTemplate, Long id) {
        var authorName = jdbcTemplate.query(
            "SELECT author_name FROM authors where id=?",
            (RowMapper<Object>) (rs, rowNum) -> rs.getNString("author_name"),
            id
        );
        log.info("author name: {}", authorName);
        var authorAddress = jdbcTemplate.query(
            "SELECT address FROM authors_detail where author_id=?",
            (RowMapper<Object>) (rs, rowNum) -> rs.getNString("address"),
            id
        );
        log.info("detail address: {}", authorAddress);

    }

    @Bean
    CommandLineRunner commandLineRunner (RoleService roleService, UserService userService,
                                         JdbcTemplate jdbcTemplate) {
        return args -> {
            DepartmentEntity it = DepartmentEntity
                .builder()
                .name("IT" + UUID.randomUUID())
                .build();

            it = userService.save(it);

            var userProfile = UserProfileEntity
                .builder()
                .gender(Gender.MALE)
                .profileName(String.format("nam-%s", UUID.randomUUID()))
                .hobbies(Set.of("of"))
                .build();
            var userProfile2 = UserProfileEntity
                .builder()
                .gender(Gender.MALE)
                .profileName(String.format("nam2-%s", UUID.randomUUID()))
                .hobbies(Set.of("of"))
                .build();

            var user = UserEntity
                .builder()
                .userName(String.format("james%15s", UUID.randomUUID()))
                .password("password")
                .email(String.format("james+%10s@gmail.com", UUID.randomUUID()))
                .department(it)
                .avatar("Byte Array Demo")
                .disabled(false)
                .build();
            user.addProfile(userProfile);
            user.addProfile(userProfile2);
            var userInDb = userService.save(user);

//            UserEntity db = userService.findOne(userInDb.getId()).get();
            var db = userService.getUser(userInDb.getId());
//            var db = userService.findUserByUsername(userInDb.getUserName()).get();
            System.out.println(db.getDepartment().getName());
            var b1 = db.getAvatar();
            log.info("{}", b1);
            var authorName = jdbcTemplate.query(
                "SELECT users.user_name,users.disabled,users.version, users_ext.user_avatar FROM users left join " +
                "users_ext on users_ext.id = users.id " +
                "where users.id=?",
                (rs, rowNum) ->
                    List.of(
                        rs.getString("disabled"),
                        rs.getString("user_name"),
                        rs.getLong("version"),
                        new String(rs.getBytes("user_avatar"))
                    )
                ,
                userInDb.getId()
            );
            log.info("db2: {}, userId: {}", authorName, userInDb.getId());
            var userProfile3 = UserProfileEntity
                .builder()
                .profileName(String.format("profile-name-%s", UUID.randomUUID()))
                .gender(Gender.MALE)
                .build();
            var user2 = userService.addProfile(userInDb.getId(), userProfile3);

            var authorName1 = jdbcTemplate.query(
                "SELECT profile_name,gender FROM users_profile where user_id=?",
                (RowMapper<Object>) (rs, rowNum) ->
                    List.of(rs.getString("profile_name"), rs.getString("gender"))
                ,
                userInDb.getId()
            );
            log.info("db: {}, userId: {}", authorName1, userInDb.getId());

        };
    }

    public static void main (String[] args) {
        SpringApplication.exit(SpringApplication.run(SpringDataApplication.class, args), () -> 0);
    }


}
