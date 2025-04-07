package nashtech.rookies.jpa.entity;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public interface EntityBase<P extends Serializable> extends Persistable<P> {

    P getId ();

    default boolean isNew () {
        return null == getId();
    }

}
