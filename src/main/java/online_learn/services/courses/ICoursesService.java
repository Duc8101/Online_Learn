package online_learn.services.courses;

import jakarta.servlet.http.HttpSession;
import online_learn.responses.ResponseBase;

public interface ICoursesService {

    ResponseBase list(String categoryId, String orderBy, String page, Integer studentId);

    ResponseBase detail(int courseId, Integer studentId);

    ResponseBase enrollCourse(int courseId, int studentId);

    ResponseBase learnCourse(int courseId, String video /* file video */, String name /*video name or pdf name*/, String pdf /*file PDF */, Integer lessonId, Integer videoId, Integer pdfId, HttpSession session);
}
