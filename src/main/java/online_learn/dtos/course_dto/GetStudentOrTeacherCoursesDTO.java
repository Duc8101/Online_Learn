package online_learn.dtos.course_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class GetStudentOrTeacherCoursesDTO {

    public GetStudentOrTeacherCoursesDTO() {
    }

    public GetStudentOrTeacherCoursesDTO(int courseId, @NonNull String courseName, @NonNull String image, int categoryId, int creatorId, @NonNull String creatorName, @Nullable String description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.image = image;
        this.categoryId = categoryId;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.description = description;
    }

    private int courseId;

    @NonNull
    private String courseName;

    @NonNull
    private String image;

    private int categoryId;
    private int creatorId;

    @NonNull
    private String creatorName;

    @Nullable
    private String description;
}
