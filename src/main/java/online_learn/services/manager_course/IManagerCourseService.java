package online_learn.services.manager_course;

import jakarta.servlet.http.HttpSession;
import online_learn.responses.ResponseBase;

public interface IManagerCourseService {

    ResponseBase list(int page, HttpSession session);

    ResponseBase create();
}
