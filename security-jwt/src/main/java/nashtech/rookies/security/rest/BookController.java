package nashtech.rookies.security.rest;

import java.net.URI;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import nashtech.rookies.jpa.entity.Author;
import nashtech.rookies.jpa.entity.Book;
import nashtech.rookies.jpa.service.AuthorService;
import nashtech.rookies.jpa.service.BookService;
import nashtech.rookies.security.dto.BookDto;

@RestController
public class BookController extends V1Rest {

    private final BookService   bookService;
    private final AuthorService authorService;

    public BookController (BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @PostMapping("/books")
    ResponseEntity<Void> save (@RequestBody @Valid BookDto data) {
        var authors = new HashSet<Author>();
        // this code has issue, could you get it?
        data.authors().forEach(e -> authorService.findOne(Long.valueOf(e)));
        Book book = new Book();
        book.setName(data.name());
        book.setCover(data.cover());
        book.setAuthors(authors);
        bookService.save(book);
        return ResponseEntity.created(URI.create("/api/v1/books/" + book.getId())).build();
    }

    @GetMapping("/books")
    ResponseEntity<List<BookDto>> getAllBooks () {
        var books = bookService.findAll();
        var bookDtos = new LinkedList<BookDto>();
        for (var book : books) {
            // if we build the list authors IDs here, the error no session will be occurred, how we can handle it
            bookDtos.add(new BookDto(book.getId(), book.getName(), book.getCover(), List.of()));
        }
        return ResponseEntity.ok(bookDtos);

    }

    @PutMapping("/books/{bookId}")
    @RolesAllowed("ROLE_ADMIN")
    ResponseEntity<BookDto> updateBook (@PathVariable("bookId") Long bookId, @RequestBody BookDto bookDto) {
        return bookService.findOne(bookId)
            .map(e -> {
                // we need add validate and update at here
                return ResponseEntity.ok(new BookDto(e.getId(), e.getName(), e.getCover(), List.of()));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());

    }
}
