package online_learn.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import online_learn.enums.Answers;

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

        for (Answers answer : Answers.values()) {
            if (answer.getValue() == integer) {
                return answer;
            }
        }
        return null;
    }
}
