package online_learn.dtos.course_dto;

import lombok.Getter;
import lombok.Setter;
import online_learn.dtos.lesson_dto.LessonListForCourseDetailOrViewLessonDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseDetailDTO extends GetAllCoursesDTO {

    private List<LessonListForCourseDetailOrViewLessonDTO> lessonListDTOs = new ArrayList<>();
}
