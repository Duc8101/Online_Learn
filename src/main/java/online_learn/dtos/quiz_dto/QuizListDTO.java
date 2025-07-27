package online_learn.dtos.quiz_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class QuizListDTO {

    public QuizListDTO(int quizId, @NonNull String quizName, int lessonId) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.lessonId = lessonId;
    }

    private int quizId;

    @NonNull
    private String quizName;

    private int lessonId;
}
