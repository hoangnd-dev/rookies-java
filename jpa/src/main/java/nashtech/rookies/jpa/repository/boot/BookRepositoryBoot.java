package nashtech.rookies.jpa.repository.boot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nashtech.rookies.jpa.entity.Book;
import nashtech.rookies.jpa.repository.BookRepository;

@Repository
public interface BookRepositoryBoot extends BookRepository, JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<Book> findAllByNameLikeIgnoreCaseOrderById (String name);

    @Query("SELECT b from Book b where lower(b.name) like lower(:name) order by b.id")
    List<Book> getAllBookByQuery (String name);
}
