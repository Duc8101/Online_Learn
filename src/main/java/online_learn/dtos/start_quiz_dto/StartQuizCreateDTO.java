package online_learn.dtos.start_quiz_dto;

import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;
import online_learn.converters.AnswerConverter;
import online_learn.enums.Answers;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class StartQuizCreateDTO {

    private int questionId;
    private int quizId;

    @Nullable
    @Convert(converter = AnswerConverter.class)
    private Answers answer;
}
