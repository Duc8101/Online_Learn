package online_learn.controllers;

import online_learn.responses.ResponseBase;
import online_learn.services.forgot_password.IForgotPasswordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

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
}
