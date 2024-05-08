package nashtech.rookies.jpa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import nashtech.rookies.jpa.entity.Author;
import nashtech.rookies.jpa.entity.AuthorDetail;
import nashtech.rookies.jpa.service.AuthorService;
import nashtech.rookies.jpa.service.BookService;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
@Log4j2
public class SpringDataApplication {

    @Bean
    CommandLineRunner commandLineRunner (AuthorService authorService, BookService bookService) {
        return args -> {
            Author author = new Author();
            author.setEmail("tech@yahoo.com");
            author.setName("this ny my name");
            AuthorDetail authorDetail = new AuthorDetail();
            authorDetail.setAddress("address");
//        authorDetail.setAuthor(author);
            author.setAuthorDetail(authorDetail);
            authorService.save(author);
            log.info(author.getId());
            log.info(author.getAuthorDetail().getAddress());

            author = new Author();
            author.setEmail("tech1@yahoo.com");
            author.setName("this ny myname");
            authorService.save(author);
            log.info(author.getId());

        };
    }

    public static void main (String[] args) {
        SpringApplication.run(SpringDataApplication.class, args);
    }

}
