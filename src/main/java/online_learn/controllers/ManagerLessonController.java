package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.manager_lesson.IManagerLessonService;
import online_learn.utils.ParseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ManagerLesson")
public class ManagerLessonController {

    private final IManagerLessonService service;

    public ManagerLessonController(IManagerLessonService service) {
        this.service = service;
    }

    @GetMapping("/{courseId}")
    public ModelAndView list(@PathVariable(required = false, value = "courseId") String CourseId, String video, String name, String pdf, Integer lessonId, HttpSession session) {
        if (ParseUtil.intTryParse(CourseId)) {
            int courseId = Integer.parseInt(CourseId);
            ResponseBase responseBase = service.list(courseId, video, name, pdf, lessonId, session);
            if (responseBase.getCode() == StatusCodeConst.NOT_FOUND) {
                return new ModelAndView("redirect:/ManagerCourse");
            }

            if (responseBase.getCode() == StatusCodeConst.OK) {
                return new ModelAndView("manager_lesson/list", responseBase.getData());
            }
            return new ModelAndView("shared/error", responseBase.getData());
        }
        /* if (ParseUtil.intTryParse(CourseId)) {
            int courseId = Integer.parseInt(CourseId);
            ResponseBase responseBase = service.list(courseId, video, name, pdf, lessonId, session);
            if (responseBase.getCode() == StatusCodeConst.NOT_FOUND) {
                return new ModelAndView("redirect:/ManagerCourse");
            }

            if (responseBase.getCode() == StatusCodeConst.OK) {
                return new ModelAndView("manager_lesson/list", responseBase.getData());
            }
            return new ModelAndView("shared/error", responseBase.getData());
        }*/
        return new ModelAndView("redirect:/Courses");
    }
}



