package nashtech.rookies.jpa.repository.boot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nashtech.rookies.jpa.entity.Author;
import nashtech.rookies.jpa.entity.Book;
import nashtech.rookies.jpa.repository.AuthorRepository;

@Repository
public interface AuthorRepositoryBoot extends AuthorRepository, JpaRepository<Author, Long> {

    @Query("SELECT b from Book b where lower(b.name) like lower(:name) order by b.id")
    List<Book> getAuthorByQuery (String name);
}
