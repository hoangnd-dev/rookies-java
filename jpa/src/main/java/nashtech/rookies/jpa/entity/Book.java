package nashtech.rookies.jpa.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOK_ID", foreignKey = @ForeignKey(name = "PAGE_BOOK_FK"))
    Set<Page> pages;

    @ManyToMany(mappedBy = "books")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Set<Author> authors;

    @Column(name = "BOOK_COVER", table = "BOOKS_DETAIL")
    String cover;
}
