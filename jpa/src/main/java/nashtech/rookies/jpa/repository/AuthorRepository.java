package nashtech.rookies.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.Author;

@NoRepositoryBean
public interface AuthorRepository extends CrudRepository<Author, Long> {
}
