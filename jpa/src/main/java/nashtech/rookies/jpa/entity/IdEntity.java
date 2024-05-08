package nashtech.rookies.jpa.entity;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class IdEntity<P extends Serializable> implements Persistable<P> {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private P id;

    @Transient
    @Override
    public boolean isNew () {
        return null == getId();
    }

}
