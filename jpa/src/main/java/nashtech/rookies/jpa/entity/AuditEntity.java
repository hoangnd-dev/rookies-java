package nashtech.rookies.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class AuditEntity<P extends Serializable> extends IdEntity<P> implements Persistable<P> {

    @Column(name = "DATE_CREATED")
    @CreatedDate
    LocalDateTime dateCreated;

    @Column(name = "DATE_MODIFIED")
    @LastModifiedDate
    LocalDateTime dateModified;

    @Transient
    @Override
    public boolean isNew () {
        return null == getId();
    }
}
