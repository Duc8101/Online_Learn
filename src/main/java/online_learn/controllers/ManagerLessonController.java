package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.utils.ParseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ManagerLesson")
public class ManagerLessonController {

    @GetMapping({"", "/{courseId}"})
    public ModelAndView learnCourse(@PathVariable(required = false, value = "courseId") String CourseId, String video, String name, String pdf, Integer lessonId, Integer videoId, Integer pdfId, HttpSession session) {
        if (ParseUtil.intTryParse(CourseId)) {
            int courseId = Integer.parseInt(CourseId);
            ResponseBase responseBase = service.learnCourse(courseId, video, name, pdf, lessonId, videoId, pdfId, session);
            if (responseBase.getCode() == StatusCodeConst.BAD_REQUEST) {
                return new ModelAndView("redirect:/MyCourse");
            }

            if (responseBase.getCode() == StatusCodeConst.OK) {
                return new ModelAndView("courses/learn_course", responseBase.getData());
            }
            return new ModelAndView("shared/error", responseBase.getData());
        }
        return new ModelAndView("redirect:/Courses");
    }
}
