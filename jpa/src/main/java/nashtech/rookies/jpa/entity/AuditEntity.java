package nashtech.rookies.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class AuditEntity<P extends Serializable> extends IdEntity<P> implements Persistable<P> {

    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @PrePersist
    private void onPersist () {
        var now = LocalDateTime.now();
        this.dateCreated = now;
        this.dateModified = now;
    }

    @PreUpdate
    private void onUpdate () {
        this.dateModified = LocalDateTime.now();
    }

}
