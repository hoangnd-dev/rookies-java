package nashtech.rookies.jpa;


import nashtech.rookies.jpa.config.ApplicationBoot;
import nashtech.rookies.jpa.entity.Author;
import nashtech.rookies.jpa.entity.AuthorDetail;
import nashtech.rookies.jpa.entity.Book;
import nashtech.rookies.jpa.service.AuthorService;
import nashtech.rookies.jpa.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Log4j2
public class JPAApp {

    private final BookService bookService;
    private final AuthorService authorService;
    private final JdbcTemplate jdbcTemplate;

    public JPAApp(BookService bookService, AuthorService authorService, JdbcTemplate jdbcTemplate) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.jdbcTemplate = jdbcTemplate;
    }

    void showBookRelation(Long id) {
        var authorName = jdbcTemplate.query("SELECT author_id,book_id FROM authors_books where book_id=?",
                (RowMapper<Object>) (rs, rowNum) -> rs.getNString("author_id")
                ,
                id
        );
        log.info("author_id: {}, book_id: {}", authorName, id);
    }

    void showAuthorInfo(Long id) {
        var authorName = jdbcTemplate.query("SELECT author_name FROM authors where id=?",
                (RowMapper<Object>) (rs, rowNum) -> rs.getNString("author_name"),
                id);
        log.info("author name: {}", authorName);
        var authorAddress = jdbcTemplate.query("SELECT address FROM authors_detail where author_id=?",
                (RowMapper<Object>) (rs, rowNum) -> rs.getNString("address"),
                id);
        log.info("detail address: {}", authorAddress);

    }

    public void run(String[] args) {

//        Author author = new Author();
//        author.setEmail("tech@yahoo.com");
//        author.setName("this ny my name");
//        AuthorDetail authorDetail = new AuthorDetail();
//        authorDetail.setAddress("address");
//        authorDetail.setAuthor(author);
//        author.setAuthorDetail(authorDetail);
//        authorService.save(author);
//        showAuthorInfo(author.getId());
//        authorService.delete(author.getId());
//        showAuthorInfo(author.getId());
//
//
        var author2 = new Author();
        author2.setEmail("tech1@yahoo.com");
        author2.setName("this ny my name 2");
        authorService.save(author2);
        log.info("author id: {}", author2.getId());



//        log.info(this.bookService);
        var book2 = new Book();
        book2.setName("Book2");
        bookService.save(book2);
        log.info("book id {}", book2.getId());

//        book.setAuthors(Set.of(author2));
        book2.getAuthors().add(author2);

        bookService.save(book2);
        log.info("book id {}", book2.getId());
        showBookRelation(book2.getId());
//        book.setAuthors(
//        var a = this.bookService.findAll();
//        a.forEach(e -> log.info(e.getId()));
    }


    public static void main(String[] args) {
        ApplicationBoot.with(JPAApp.class).run(args);
    }
}
