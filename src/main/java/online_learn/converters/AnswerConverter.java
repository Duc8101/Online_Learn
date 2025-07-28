package online_learn.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import online_learn.enums.Answers;

import java.util.Arrays;

@Converter
public class AnswerConverter implements AttributeConverter<Answers, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Answers answers) {
        return answers == null ? null : answers.getValue();
    }

    @Override
    public Answers convertToEntityAttribute(Integer integer) {
        if (integer == null) {
            return null;
        }
        return Arrays.stream(Answers.values()).filter(a -> a.getValue() == integer).findFirst().orElse(null);
    }
}
