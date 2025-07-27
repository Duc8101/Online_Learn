package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.result.IResultService;
import online_learn.utils.ParseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/Result")
public class ResultController {

    private final IResultService service;

    public ResultController(IResultService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index(String quizId,  HttpSession session) {
        if (ParseUtil.intTryParse(quizId)) {
            ResponseBase responseBase = service.index(Integer.parseInt(quizId), session);
            if (responseBase.getCode() == StatusCodeConst.OK) {
                return new ModelAndView("result/index", responseBase.getData());
            }
            return new ModelAndView("shared/error", responseBase.getData());
        }
        return new ModelAndView("redirect:/MyCourse");
    }
}
