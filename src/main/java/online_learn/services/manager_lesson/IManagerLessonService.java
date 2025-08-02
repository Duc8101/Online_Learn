package online_learn.services.manager_lesson;

import jakarta.servlet.http.HttpSession;
import online_learn.dtos.lesson_dto.LessonCreateDTO;
import online_learn.dtos.lesson_dto.LessonUpdateDTO;
import online_learn.responses.ResponseBase;

public interface IManagerLessonService {

    ResponseBase list(int courseId, String video, String name, String pdf, Integer lessonId, HttpSession session);

    ResponseBase create(LessonCreateDTO DTO);

    ResponseBase update(int lessonId, LessonUpdateDTO DTO);
}
