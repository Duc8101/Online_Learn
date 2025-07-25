package online_learn.services.courses;

import online_learn.responses.ResponseBase;

public interface ICoursesService {

    ResponseBase list(String categoryId, String orderBy, String page, Integer studentId);
    ResponseBase detail(int courseId, Integer studentId);
}
