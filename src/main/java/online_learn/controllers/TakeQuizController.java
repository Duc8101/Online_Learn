package online_learn.controllers;

import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.take_quiz.ITakeQuizService;
import online_learn.utils.ParseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/TakeQuiz")
public class TakeQuizController {

    private final ITakeQuizService service;

    public TakeQuizController(ITakeQuizService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index(String lessonId) {
        if (ParseUtil.intTryParse(lessonId)) {
            ResponseBase responseBase = service.index(Integer.parseInt(lessonId));
            if (responseBase.getCode() == StatusCodeConst.OK) {
                return new ModelAndView("take_quiz/index", responseBase.getData());
            }

            if (responseBase.getCode() == StatusCodeConst.BAD_REQUEST) {
                return new ModelAndView("redirect:/MyCourse");
            }
            return new ModelAndView("shared/error", responseBase.getData());
        }
        return new ModelAndView("redirect:/Courses");
    }
}
