package nashtech.rookies.jpa.entity;


import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class UserProfileId implements Serializable {
    @EqualsAndHashCode.Include
    private UUID user;
    @EqualsAndHashCode.Include
    private String profileName;
}
