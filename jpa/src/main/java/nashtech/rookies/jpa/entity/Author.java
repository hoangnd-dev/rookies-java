package nashtech.rookies.jpa.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "AUTHORS_BOOKS",
        joinColumns = @JoinColumn(name = "AUTHOR_ID"),
        inverseJoinColumns = @JoinColumn(name = "BOOK_ID"))
    Set<Book> books;


    @OneToOne(mappedBy = "author", cascade = CascadeType.ALL)
    AuthorDetail authorDetail;

    @Version
    @Getter(AccessLevel.NONE)
    private int version;




}
