package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.enums.Roles;
import online_learn.responses.ResponseBase;
import online_learn.services.courses.ICoursesService;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.utils.ParseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/Courses")
public class CoursesController {

    private final ICoursesService service;

    public CoursesController(ICoursesService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView list(String categoryId, String orderBy, String page, HttpSession session) {
        if ((categoryId != null && !ParseUtil.intTryParse(categoryId)) || (page != null && !ParseUtil.intTryParse(page))
                || (orderBy != null && !ParseUtil.booleanTryParse(orderBy))) {
            return new ModelAndView("redirect:/Home");
        }

        UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
        ResponseBase responseBase;
        if (user == null || user.getRoleId() != Roles.STUDENT.getValue()) {
            responseBase = service.list(categoryId, orderBy, page, null);
        } else {
            responseBase = service.list(categoryId, orderBy, page, user.getUserId());
        }

        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("courses/list", responseBase.getData());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }

    @GetMapping({"/Detail", "/Detail/{courseId}"})
    public ModelAndView detail(@PathVariable(required = false, value = "courseId") String CourseId, HttpSession session) {
        if (ParseUtil.intTryParse(CourseId)) {
            int courseId = Integer.parseInt(CourseId);
            UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
            ResponseBase responseBase;
            if (user == null || user.getRoleId() != Roles.STUDENT.getValue()) {
                responseBase = service.detail(courseId, null);
            } else {
                responseBase = service.detail(courseId, user.getUserId());
            }

            if (responseBase.getCode() == StatusCodeConst.OK) {
                return new ModelAndView("courses/detail", responseBase.getData());
            }
            return new ModelAndView("shared/error", responseBase.getData());
        }
        return new ModelAndView("redirect:/Courses");
    }

    @GetMapping({"/EnrollCourse", "/EnrollCourse/{courseId}"})
    public ModelAndView enrollCourse(@PathVariable(required = false, value = "courseId") String CourseId, HttpSession session) {
        UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
        if (user == null) {
            return new ModelAndView("redirect:/Login");
        }

        if (ParseUtil.intTryParse(CourseId)) {
            int courseId = Integer.parseInt(CourseId);
            ResponseBase responseBase = service.enrollCourse(courseId, user.getUserId());
            if (responseBase.getCode() == StatusCodeConst.OK) {
                return new ModelAndView("redirect:/MyCourse");
            }
            return new ModelAndView("shared/error", responseBase.getData());
        }
        return new ModelAndView("redirect:/Courses");
    }

    @GetMapping({"/LearnCourse", "/LearnCourse/{courseId}"})
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
