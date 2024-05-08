package nashtech.rookies.jpa.repository.noboot;

import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.Author;
import nashtech.rookies.jpa.repository.AuthorRepository;

@NoRepositoryBean
public class AuthorRepositoryNoBoot extends GenericJPA<Author, Long> implements AuthorRepository {

}
