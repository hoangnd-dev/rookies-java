package nashtech.rookies.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthorDto(Long id, @JsonProperty("author_name") String name, String email) {
}
