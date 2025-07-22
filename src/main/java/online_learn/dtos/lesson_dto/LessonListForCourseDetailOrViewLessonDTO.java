package online_learn.dtos.lesson_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonListForCourseDetailOrViewLessonDTO {

    private int lessonId;
    private String lessonName;
    private int courseId;

    public LessonListForCourseDetailOrViewLessonDTO() {
    }

    public LessonListForCourseDetailOrViewLessonDTO(int lessonId, String lessonName, int courseId) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.courseId = courseId;
    }
}
