package nashtech.rookies.jpa.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn (Boolean attribute) {
        return attribute != null && attribute ? "YES" : "NO";
    }

    @Override
    public Boolean convertToEntityAttribute (String dbData) {
        return "YES".equals(dbData);
    }
}
