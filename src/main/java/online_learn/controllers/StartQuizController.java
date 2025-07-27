package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.start_quiz_dto.StartQuizCreateDTO;
import online_learn.responses.ResponseBase;
import online_learn.services.start_quiz.IStartQuizService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/StartQuiz")
public class StartQuizController {

    private final IStartQuizService service;

    public StartQuizController(IStartQuizService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index(int quizId, HttpSession session) {
        ResponseBase responseBase = service.index(quizId, session);

        // if not enroll course or not questions in quiz
        if (responseBase.getCode() == StatusCodeConst.BAD_REQUEST) {
            Integer lessonId = (Integer) responseBase.getData().get("lessonId");
            // if not enroll course
            if (lessonId == null) {
                return new ModelAndView("redirect:/MyCourse");
            }
            return new ModelAndView(String.format("redirect:/TakeQuiz?lessonId=%d", lessonId));
        }

        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("start_quiz/index", responseBase.getData());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }

    @PostMapping("")
    public ModelAndView index(StartQuizCreateDTO DTO, int minutes, int questionNo, int seconds, String button, HttpSession session) {
        // if move to next question
        if (button != null && button.equals("Next")) {
            ResponseBase responseBase = service.next(DTO, minutes, questionNo, seconds, session);
            if (responseBase.getCode() == StatusCodeConst.OK) {
                return new ModelAndView("start_quiz/index", responseBase.getData());
            }
            return new ModelAndView("shared/error", responseBase.getData());
        }

        // if move to previous question
        if (button != null && button.equals("Back")) {
            ResponseBase responseBase = service.previous(DTO, minutes, questionNo, seconds, session);
            if (responseBase.getCode() == StatusCodeConst.OK) {
                return new ModelAndView("start_quiz/index", responseBase.getData());
            }
            return new ModelAndView("shared/error", responseBase.getData());
        }

        ResponseBase responseBase = service.finish(DTO, session);
        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView(String.format("redirect:/Result?quizId=%d", DTO.getQuizId()));
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }
}
