package online_learn.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import online_learn.enums.Genders;

import java.util.Arrays;

@Converter
public class GenderConverter implements AttributeConverter<Genders, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Genders genders) {
        return genders == null ? null : genders.getValue();
    }

    @Override
    public Genders convertToEntityAttribute(Integer integer) {
        if (integer == null) {
            return null;
        }
        return Arrays.stream(Genders.values()).filter(g -> g.getValue() == integer).findFirst().orElse(null);
    }
}
