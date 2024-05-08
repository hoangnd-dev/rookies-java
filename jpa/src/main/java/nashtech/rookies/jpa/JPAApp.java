package nashtech.rookies.jpa;


import nashtech.rookies.jpa.config.ApplicationBoot;
import nashtech.rookies.jpa.entity.Author;
import nashtech.rookies.jpa.entity.AuthorDetail;
import nashtech.rookies.jpa.service.AuthorService;
import nashtech.rookies.jpa.service.BookService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JPAApp {

    private final BookService   bookService;
    private final AuthorService authorService;

    public JPAApp (BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }


    public void run (String[] args) {

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

//        log.info(this.bookService);
//        Book book = new Book();
//        book.setAuthors(
//        var a = this.bookService.findAll();
//        a.forEach(e -> log.info(e.getId()));
    }


    public static void main (String[] args) {
        ApplicationBoot.with(JPAApp.class).run(args);
    }
}
