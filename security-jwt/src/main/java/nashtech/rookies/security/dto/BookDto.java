package nashtech.rookies.security.dto;

import java.util.List;

public record BookDto(Long bookId, String name, String cover, List<String> authors) {
}
