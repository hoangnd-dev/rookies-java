package nashtech.rookies.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.Book;

@NoRepositoryBean
public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAllByNameLikeIgnoreCaseOrderById(String name);

    @Query("SELECT b from Book b where lower(b.name) like lower(:name) order by b.id")
    List<Book> getAllBookByQuery(String name);
}
