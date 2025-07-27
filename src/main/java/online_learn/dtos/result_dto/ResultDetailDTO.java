package online_learn.dtos.result_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ResultDetailDTO {

    private int resultId;
    private int quizId;
    private int studentId;

    @NonNull
    private String studentName;

    private double score;

    @NonNull
    private String status;
}
