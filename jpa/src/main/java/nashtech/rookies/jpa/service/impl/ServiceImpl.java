package nashtech.rookies.jpa.service.impl;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import nashtech.rookies.jpa.entity.EntityBase;
import nashtech.rookies.jpa.repository.Repository;
import nashtech.rookies.jpa.service.Service;

@Transactional(readOnly = true)
public abstract class ServiceImpl<T extends EntityBase<?>, ID> implements Service<T, ID> {

    protected final Repository<T, ID> repository;

    ServiceImpl (Repository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public T save (T entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<T> findOne (ID pk) {
        return this.repository.findById(pk);
    }

    @Override
    public Iterable<T> findAll () {
        return this.repository.findAll();
    }

    @Override
    public void delete (ID id) {
        this.repository.deleteById(id);
    }
}
