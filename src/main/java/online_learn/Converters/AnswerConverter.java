package online_learn.Converters;

import lombok.NonNull;
import online_learn.Enums.Answers;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AnswerConverter implements Converter<Integer, Answers> {

    @Override
    public Answers convert(@NonNull Integer source) {
        for (Answers answer : Answers.values()) {
            if (answer.getValue() == source) {
                return answer;
            }
        }
        return null;
    }
}
