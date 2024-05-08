package nashtech.rookies.jpa.repository.noboot;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.Book;
import nashtech.rookies.jpa.repository.BookRepository;

@NoRepositoryBean
public class BookRepositoryNoBoot extends GenericJPA<Book, Long> implements BookRepository {


    @Override
    public List<Book> findAllByNameLikeIgnoreCaseOrderById (String name) {
        return List.of();
    }

    @Override
    public List<Book> getAllBookByQuery (String name) {
        return List.of();
    }

}
