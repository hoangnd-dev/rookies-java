package nashtech.rookies.jpa.repository.noboot;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoRepositoryBean
public abstract class GenericJPA<T, ID> {


    @PersistenceContext
    private EntityManager em;

    @Autowired
    PlatformTransactionManager transactionManager;

    private TransactionTemplate transactionTemplate;

    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    GenericJPA () {
        var type = getClass().getGenericSuperclass();
        ParameterizedType p = (ParameterizedType) type;
        this.clazz = (Class<T>) (p.getActualTypeArguments()[0]);
    }

    @PostConstruct
    void init () {
        transactionTemplate = new TransactionTemplate(this.transactionManager);
    }

    public Optional<T> findOneById (ID id) {
        return Optional.ofNullable(em.find(this.clazz, id));
    }

    public <S extends T> S save (S entity) {
        return doInTransaction(entityManager -> {
            em.persist(entity);
            return entity;
        });

    }

    public List<T> getAll () {
        var cb = em.getCriteriaBuilder();
        var cr = cb.createQuery(this.clazz);
        var from = cr.from(this.clazz);
        cr.select(from);
        return em.createQuery(cr).getResultList();
    }


    public <S extends T> S doInTransaction (Function<EntityManager, S> func) {
        return transactionTemplate.execute(status -> func.apply(em));
    }

    public <S extends T> Iterable<S> saveAll (Iterable<S> entities) {
        return null;
    }

    public Optional<T> findById (ID aLong) {
        return Optional.ofNullable(em.find(this.clazz, aLong));
    }

    public boolean existsById (ID aLong) {
        return findById(aLong).isPresent();
    }

    public Iterable<T> findAll () {
        return this.getAll();
    }

    public Iterable<T> findAllById (Iterable<ID> longs) {
        return null;
    }

    public long count () {
        return 0;
    }

    public void deleteById (ID aLong) {
        delete(findById(aLong).orElseThrow());
    }

    public void delete (T entity) {
        em.remove(entity);
    }

    public void deleteAllById (Iterable<? extends ID> longs) {

    }

    public void deleteAll (Iterable<? extends T> entities) {

    }

    public void deleteAll () {

    }

}
