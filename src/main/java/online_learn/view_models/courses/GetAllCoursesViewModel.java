package online_learn.view_models.courses;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class GetAllCoursesViewModel extends GetStudentOrTeacherCoursesViewModel {

    private boolean lessonExist;
    private boolean enrollCourseExist;

    public GetAllCoursesViewModel() {
    }

    public GetAllCoursesViewModel(int courseId, @NonNull String courseName, @NonNull String image, int categoryId, int creatorId, @NonNull String creatorName, String description, boolean lessonExist, boolean enrollCourseExist) {
        super(courseId, courseName, image, categoryId, creatorId, creatorName, description);
        this.lessonExist = lessonExist;
        this.enrollCourseExist = enrollCourseExist;
    }
}
