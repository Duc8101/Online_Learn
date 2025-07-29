package online_learn.services.manager_course;

import jakarta.servlet.http.HttpSession;
import online_learn.dtos.course_dto.CourseCreateUpdateDTO;
import online_learn.responses.ResponseBase;

public interface IManagerCourseService {

    ResponseBase list(int page, HttpSession session);

    ResponseBase create();

    ResponseBase create(CourseCreateUpdateDTO DTO, HttpSession session);

    ResponseBase update(int courseId, HttpSession session);

    ResponseBase update(int courseId, CourseCreateUpdateDTO DTO, HttpSession session);
}
