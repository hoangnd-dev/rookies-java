package nashtech.rookies.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "AUTHORS_DETAIL")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
public class AuthorDetail extends IdEntity<Long> {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AUTHOR_ID", foreignKey = @ForeignKey(name = "AUTHORS_ID_FK"))
    @ToString.Exclude
    Author author;

    String address;

    @Version
    private int version;


}
