package nashtech.rookies.jpa.service.impl;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import nashtech.rookies.jpa.service.Service;

public abstract class ServiceImpl<T, ID> implements Service<T, ID> {

    protected final CrudRepository<T, ID> repository;

    ServiceImpl (CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public T save (T entity) {
        this.repository.save(entity);
        return entity;
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
