package online_learn.dtos.course_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class CourseCreateUpdateDTO {

    @NonNull
    private String courseName;

    @NonNull
    private String image;

    private int categoryId;

    @Nullable
    private String description;
}
