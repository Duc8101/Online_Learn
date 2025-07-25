package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.my_course.IMyCourseService;
import online_learn.utils.ParseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/MyCourse")
public class MyCourseController {

    private final IMyCourseService service;

    public MyCourseController(IMyCourseService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index(String page, HttpSession session) {
        if (page != null && !ParseUtil.intTryParse(page)) {
            return new ModelAndView("redirect:/Home");
        }

        ResponseBase responseBase = service.index(page, session);
        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("my_course/index", responseBase.getData());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }
}
