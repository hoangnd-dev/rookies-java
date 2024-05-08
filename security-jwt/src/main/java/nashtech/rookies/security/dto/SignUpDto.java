package nashtech.rookies.security.dto;

import java.util.List;

public record SignUpDto(String username, String password, List<String> roles) {
}
