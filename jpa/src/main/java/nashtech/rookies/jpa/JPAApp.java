package nashtech.rookies.jpa;


import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nashtech.rookies.jpa.config.ApplicationBoot;
import nashtech.rookies.jpa.entity.Gender;
import nashtech.rookies.jpa.entity.UserEntity;
import nashtech.rookies.jpa.entity.UserProfileEntity;
import nashtech.rookies.jpa.repository.UserRepository;
import nashtech.rookies.jpa.service.RoleService;
import nashtech.rookies.jpa.service.UserService;

@Log4j2
@RequiredArgsConstructor
public class JPAApp {

    private final RoleService    roleService;
    private final JdbcTemplate   jdbcTemplate;
    private final UserService    userService;
    private final UserRepository userRepository;


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

    public void run (String[] args) {
        var user = UserEntity
            .builder()
            .userName("james")
            .password("password")
            .email("james@gmail.com")
            .avatar("Byte Array Demo".getBytes())
            .disabled(true)
            .build();

        var userInDb = userService.save(user);

        var db = userRepository.findById(userInDb.getId()).get();
        var b1 = db.getAvatar();
        System.out.println(new String(b1));
        var authorName = jdbcTemplate.query(
            "SELECT users.user_name,users.disabled,users.version, users_ext.user_avatar FROM users left join " +
            "users_ext on users_ext.id = users.id " +
            "where users.id=?",
            (rs, rowNum) ->
                List.of(
                    rs.getNString("disabled"),
                    rs.getNString("user_name"),
                    rs.getLong("version"),
                    new String(rs.getBytes("user_avatar"))
                )
            ,
            userInDb.getId()
        );
        log.info("db2: {}, userId: {}", authorName, userInDb.getId());
        var userProfile = UserProfileEntity
            .builder()
            .profileName("PROFILE NAME")
            .user(userInDb)
            .gender(Gender.MALE)
            .build();

        userService.save(userProfile);
        var authorName1 = jdbcTemplate.query(
            "SELECT profile_name,gender FROM users_profile where user_id=?",
            (RowMapper<Object>) (rs, rowNum) ->
                List.of(rs.getNString("profile_name"), rs.getNString("gender"))
            ,
            userInDb.getId()
        );
        log.info("db: {}, userId: {}", authorName1, userInDb.getId());


    }


    public static void main (String[] args) {
        ApplicationBoot.with(JPAApp.class).run(args);
    }
}
