package online_learn.dtos.lesson_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class LessonCreateDTO {

    @NonNull
    private String lessonName;

    private int courseId;
}
