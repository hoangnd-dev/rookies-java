package nashtech.rookies.jpa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.log4j.Log4j2;
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

    @Bean
    PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
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
            //            Author author = new Author();
            //            author.setEmail("tech@yahoo.com");
            //            author.setName("this ny my name");
            //            AuthorDetail authorDetail = new AuthorDetail();
            //            authorDetail.setAddress("address");
            //            authorDetail.setAuthor(author);
            //            author.setAuthorDetail(authorDetail);
            //            authorService.save(author);
            //            showAuthorInfo(jdbcTemplate, author.getId());
            //            authorService.delete(author.getId());
            //            showAuthorInfo(jdbcTemplate, author.getId());
            //
            //            author = new Author();
            //            author.setEmail("tech1@yahoo.com");
            //            author.setName("this ny myname");
            //            authorService.save(author);
            //            log.info(author.getId());

//            var book = new Book();
//            book.setName("Book2");
//            userService.save(book);
//            log.info("book id {}", book.getId());
//            var book2 = userService.findOneByNameSpecification("Book2");
//            log.info("Book {}", book2);

        };
    }

    public static void main (String[] args) {
        SpringApplication.run(SpringDataApplication.class, args);
    }

}
