package online_learn.services.courses;

import online_learn.responses.ResponseBase;

public interface ICoursesService {

    ResponseBase list(Integer categoryId, Boolean orderBy, Integer page, Integer studentId);
}
