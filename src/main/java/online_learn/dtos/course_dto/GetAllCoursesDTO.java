package online_learn.dtos.course_dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class GetAllCoursesDTO extends GetStudentOrTeacherCoursesDTO {

    private boolean lessonExist;
    private boolean enrollCourseExist;

    public GetAllCoursesDTO() {
    }

    public GetAllCoursesDTO(int courseId, @NonNull String courseName, @NonNull String image, int categoryId, int creatorId, @NonNull String creatorName, String description, boolean lessonExist, boolean enrollCourseExist) {
        super(courseId, courseName, image, categoryId, creatorId, creatorName, description);
        this.lessonExist = lessonExist;
        this.enrollCourseExist = enrollCourseExist;
    }
}
