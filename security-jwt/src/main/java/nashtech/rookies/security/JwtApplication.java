package nashtech.rookies.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.log4j.Log4j2;
import nashtech.rookies.jpa.SpringDataApplication;
import nashtech.rookies.jpa.service.RoleService;
import nashtech.rookies.security.dto.SignUpDto;
import nashtech.rookies.security.service.AuthService;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
@ComponentScan(value = { "nashtech.rookies" }, excludeFilters = { @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE, classes = { SpringDataApplication.class }) }
)
@EnableJpaRepositories({ "nashtech.rookies.jpa.repository", "nashtech.rookies.security.repository" })
@Log4j2
@EntityScan({ "nashtech.rookies.security.entity", "nashtech.rookies.jpa.entity" })
public class JwtApplication {

    @Bean
    CommandLineRunner commandLineRunner (
        @Value("${admin.default.pass}") String initPass,
        AuthService authService,
        RoleService roleService,
        RoleRepository roleRepository) {
        return args -> {
            var rolesDefault = List.of("ROLE_USER", "ROLE_ADMIN");
            List<Role> roles = rolesDefault.stream()
                .map(r -> Role.builder().roleName(r).build())
                .map(roleRepository::save)
                .toList();

            log.info("Roles {}", roles);
            var u1 = new SignUpDto("admin", initPass, rolesDefault);
            var u2 = authService.signUp(u1);
            System.out.println(u2);

            var user = new SignUpDto("user", initPass, List.of("ROLE_USER"));
            var userDb = authService.signUp(user);
            System.out.println(userDb);

//            Author author = new Author();
//            author.setEmail("tech@yahoo.com");
//            author.setName("this ny my name");
//            AuthorDetail authorDetail = new AuthorDetail();
//            authorDetail.setAddress("address");
////        authorDetail.setAuthor(author);
//            author.setAuthorDetail(authorDetail);
//            roleService.save(author);
//            log.info(author.getId());
//            log.info(author.getAuthorDetail().getAddress());

        };
    }


    public static void main (String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }

}

