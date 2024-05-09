package nashtech.rookies.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "AUTHORS")
@NoArgsConstructor
@Getter
@Setter
public class Author extends AuditEntity<Long> {

    @Column(name = "AUTHOR_NAME", length = 100, nullable = false)
    String name;

    @Column(name = "EMAIL", unique = true, nullable = false)
    @Email
    String email;

    @ManyToMany(mappedBy = "authors")
    Set<Book> books;


    @OneToOne(mappedBy = "author", cascade = CascadeType.ALL)
    AuthorDetail authorDetail;

    @Version
    @Getter(AccessLevel.NONE)
    private int version;


}
