package online_learn.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import online_learn.enums.Genders;

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

        for (Genders gender : Genders.values()) {
            if (gender.getValue() == integer) {
                return gender;
            }
        }
        return null;
    }
}
