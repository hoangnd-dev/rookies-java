package nashtech.rookies.jpa.entity;

import java.util.Set;

import org.springframework.util.StringUtils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SetConverter implements AttributeConverter<Set<String>, String> {
    static private final String separator = ";";
    @Override
    public String convertToDatabaseColumn (Set<String> strings) {
        return strings == null ? "" : String.join(separator, strings);
    }

    @Override
    public Set<String> convertToEntityAttribute (String s) {
        return StringUtils.hasLength(s) ? Set.of(s.split(separator)) : Set.of();
    }
}
