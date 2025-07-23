package online_learn.controllers;

import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.forgot_password.IForgotPasswordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ForgotPassword")
public class ForgotPasswordController {

    private final IForgotPasswordService service;

    public ForgotPasswordController(IForgotPasswordService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index() {
        ResponseBase responseBase = service.index();
        return new ModelAndView("forgot_password/index",  responseBase.getData());
    }

    @PostMapping("")
    public ModelAndView index(String email) {
        ResponseBase responseBase = service.index(email);
        if (responseBase.getCode() == StatusCodeConst.INTERNAL_SERVER_ERROR) {
            return new ModelAndView("shared/error",  responseBase.getData());
        }
        return new ModelAndView("forgot_password/index",  responseBase.getData());
    }
}
