package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.course_dto.CourseCreateUpdateDTO;
import online_learn.responses.ResponseBase;
import online_learn.services.manager_course.IManagerCourseService;
import online_learn.utils.ParseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ManagerCourse")
public class ManagerCourseController {

    private final IManagerCourseService service;

    public ManagerCourseController(IManagerCourseService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView list(String page, HttpSession session) {
        if (page != null && !ParseUtil.intTryParse(page)) {
            return new ModelAndView("redirect:/Home");
        }

        ResponseBase responseBase = service.list(page == null ? 1 : Integer.parseInt(page), session);
        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("manager_course/list", responseBase.getData());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }

    @GetMapping("/Create")
    public ModelAndView create() {
        ResponseBase responseBase = service.create();
        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("manager_course/create", responseBase.getData());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }

    @PostMapping("/Create")
    public ModelAndView create(CourseCreateUpdateDTO DTO, HttpSession session) {
        ResponseBase responseBase = service.create(DTO, session);
        if (responseBase.getCode() == StatusCodeConst.OK || responseBase.getCode() == StatusCodeConst.BAD_REQUEST) {
            return new ModelAndView("manager_course/create", responseBase.getData());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }

    @GetMapping("/Update/{courseId}")
    public ModelAndView update(@PathVariable(required = false, name = "courseId") String courseId, HttpSession session) {
        if (!ParseUtil.intTryParse(courseId)) {
            return new ModelAndView("redirect:/Courses");
        }

        ResponseBase responseBase = service.update(Integer.parseInt(courseId), session);
        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("manager_course/update", responseBase.getData());
        }

        if (responseBase.getCode() == StatusCodeConst.BAD_REQUEST) {
            return new ModelAndView("redirect:/ManagerCourse");
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }

    @PostMapping("/Update/{courseId}")
    public ModelAndView update(@PathVariable(name = "courseId") int courseId, CourseCreateUpdateDTO DTO, HttpSession session) {
        ResponseBase responseBase = service.update(courseId, DTO, session);
        if (responseBase.getCode() == StatusCodeConst.OK || responseBase.getCode() == StatusCodeConst.BAD_REQUEST) {
            return new ModelAndView("manager_course/update", responseBase.getData());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }
}
