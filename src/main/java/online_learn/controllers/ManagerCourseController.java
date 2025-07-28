package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.manager_course.IManagerCourseService;
import online_learn.utils.ParseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ModelAndView index(String page, HttpSession session) {
        if (page != null && !ParseUtil.intTryParse(page)) {
            return new ModelAndView("redirect:/Home");
        }

        ResponseBase responseBase = service.list(page == null ? 1 : Integer.parseInt(page), session);
        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("manager_course/list", responseBase.getData());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }
}
