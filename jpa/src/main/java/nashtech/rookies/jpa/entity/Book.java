package nashtech.rookies.jpa.entity;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "BOOKS")
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SecondaryTable(name = "BOOKS_DETAIL", pkJoinColumns = @PrimaryKeyJoinColumn(name = "ID"))
public class Book extends AuditEntity<Long> {

    @Column(name = "BOOK_NAME", nullable = false)
    String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "BOOK_ID", foreignKey = @ForeignKey(name = "PAGE_BOOK_FK"))
    Set<Page> pages;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "AUTHORS_BOOKS",
            joinColumns = @JoinColumn(name = "BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID"))
    Set<Author> authors;

    @Column(name = "BOOK_COVER", table = "BOOKS_DETAIL")
    String cover;
}
