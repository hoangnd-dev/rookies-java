package nashtech.rookies.jpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import nashtech.rookies.jpa.entity.EntityBase;

@NoRepositoryBean
public interface Repository<T extends EntityBase<?>, ID> extends CrudRepository<T, ID> {

}
