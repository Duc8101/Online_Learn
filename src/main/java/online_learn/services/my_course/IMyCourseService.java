package online_learn.services.my_course;

import jakarta.servlet.http.HttpSession;
import online_learn.responses.ResponseBase;

public interface IMyCourseService {

    ResponseBase index(String page, HttpSession session);
}
