package online_learn.Converters;

import lombok.NonNull;
import online_learn.Enums.Genders;
import org.springframework.core.convert.converter.Converter;

public class GenderConverter implements Converter<Integer, Genders> {

    @Override
    public Genders convert(@NonNull Integer source) {
        for(Genders gender : Genders.values() ) {
            if (gender.getValue() == source) {
                return gender;
            }
        }
        return null;
    }
}
