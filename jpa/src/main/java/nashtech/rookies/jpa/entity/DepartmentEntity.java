package nashtech.rookies.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "DEPARTMENTS")
public class DepartmentEntity extends AuditEntity<Long> {

    @Column(name = "DEPARTMENT_NAME", unique = true, nullable = false)
    String name;

}
