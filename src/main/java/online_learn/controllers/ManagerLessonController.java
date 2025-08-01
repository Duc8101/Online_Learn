package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.lesson_dto.LessonCreateDTO;
import online_learn.dtos.lesson_dto.LessonUpdateDTO;
import online_learn.responses.ResponseBase;
import online_learn.services.manager_lesson.IManagerLessonService;
import online_learn.utils.ParseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        return new ModelAndView("redirect:/Courses");
    }

    @PostMapping("/Create")
    public ModelAndView create(LessonCreateDTO DTO) {
        ResponseBase responseBase = service.create(DTO);
        if (responseBase.getCode() == StatusCodeConst.BAD_REQUEST) {
            return new ModelAndView("manager_lesson/list", responseBase.getData());
        }

        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("redirect:/ManagerLesson/" + DTO.getCourseId());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }

    @PostMapping("/Update/{lessonId}")
    public ModelAndView update(@PathVariable(value = "lessonId") int lessonId, LessonUpdateDTO DTO, int courseId) {
        ResponseBase responseBase = service.update(lessonId, DTO);
        if (responseBase.getCode() == StatusCodeConst.BAD_REQUEST) {
            return new ModelAndView("manager_lesson/list", responseBase.getData());
        }

        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("redirect:/ManagerLesson/" + courseId);
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }
}



