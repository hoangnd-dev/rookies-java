package nashtech.rookies.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtToken(@JsonProperty("access_token") String accessToken) {
}
