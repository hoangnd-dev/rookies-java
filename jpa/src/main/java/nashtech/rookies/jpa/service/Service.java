package nashtech.rookies.jpa.service;

import java.util.Optional;

@org.springframework.stereotype.Service
public interface Service<T, ID> {

    T save (T entity);

    Optional<T> findOne (ID pk);


    Iterable<T> findAll ();
}
