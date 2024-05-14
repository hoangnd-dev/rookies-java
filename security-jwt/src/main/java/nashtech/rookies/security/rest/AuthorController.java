package nashtech.rookies.security.rest;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import nashtech.rookies.jpa.service.AuthorService;
import nashtech.rookies.security.dto.AuthorDto;

@RestController
public class AuthorController extends V1Rest {
    private final AuthorService authorService;

    public AuthorController (AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    ResponseEntity<List<AuthorDto>> getAll () {
        var authors = authorService.findAll();
        var dtos = new LinkedList<AuthorDto>();
        for (var author : authors) {
            dtos.add(new AuthorDto(author.getId(), author.getName(), author.getEmail()));
        }
        return ResponseEntity.ok(dtos);
    }
}
