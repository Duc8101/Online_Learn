package online_learn.dtos.start_quiz_dto;

import lombok.Getter;
import lombok.Setter;
import online_learn.enums.Answers;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class StartQuizCreateDTO {

    private int questionId ;
    private int quizId ;

    @Nullable
    private Answers answer ;
}
