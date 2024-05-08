package nashtech.rookies.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "PAGES",
    uniqueConstraints = {
        @UniqueConstraint(name = "BOOK_PAGE_UNI", columnNames = { "PAGE_NUM", "BOOK_ID" })
    }
)
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Page extends IdEntity<Long> {

    @Column(name = "PAGE_NUM")
    Long pageNum;

    @Column(name = "BOOK_ID")
    Long bookId;
}
