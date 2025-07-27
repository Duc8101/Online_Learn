package online_learn.dtos.question_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import online_learn.enums.Answers;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class QuestionListForStartQuizDTO {

    private int questionId;

    @NonNull
    private String questionName;

    private int quizId;

    @NonNull
    private String answer1;

    @NonNull
    private String answer2;

    @NonNull
    private String answer3;

    @NonNull
    private String answer4;

    @Nullable
    private Answers chosenAnswer;
}
