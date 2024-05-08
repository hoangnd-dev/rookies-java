package nashtech.rookies.jpa.service;

import java.util.List;

import nashtech.rookies.jpa.entity.Book;

public interface BookService extends Service<Book, Long> {

    List<Book> findOneByName(String name);

    List<Book> findOneByNameQuery(String name);

    List<Book> findOneByNameSpecification(String name);

    List<Book> findOneByNameExample(String name);
}
