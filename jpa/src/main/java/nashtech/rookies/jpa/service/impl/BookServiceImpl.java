package nashtech.rookies.jpa.service.impl;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nashtech.rookies.jpa.entity.Book;
import nashtech.rookies.jpa.repository.BookRepository;
import nashtech.rookies.jpa.repository.boot.BookRepositoryBoot;
import nashtech.rookies.jpa.service.BookService;

import java.util.Collections;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl extends ServiceImpl<Book, Long> implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl (BookRepository bookRepository) {
        super(bookRepository);
        this.bookRepository = bookRepository;
    }


    @Override
    public List<Book> findOneByName (String name) {
        return this.bookRepository.findAllByNameLikeIgnoreCaseOrderById(name);
    }

    @Override
    public List<Book> findOneByNameQuery (String name) {
        return this.bookRepository.getAllBookByQuery(name);
    }

    @Override
    public List<Book> findOneByNameSpecification (String name) {
        Specification<Book> specification = (root, query, criteriaBuilder) -> {
            var namePredicate = criteriaBuilder.like(root.get("name"), name);
            return criteriaBuilder.and(namePredicate);
        };
        if ( bookRepository instanceof BookRepositoryBoot bookRepositoryBoot ) {
            return bookRepositoryBoot.findAll(specification);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Book> findOneByNameExample (String name) {
        var book = new Book();
        book.setName(name);
        var exampleMatcher = ExampleMatcher.matching()
            .withIgnoreCase()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        var example = Example.of(book, exampleMatcher);
        if ( bookRepository instanceof BookRepositoryBoot bookRepositoryBoot ) {
            return bookRepositoryBoot.findAll(example);
        }
        return Collections.emptyList();
    }


}
