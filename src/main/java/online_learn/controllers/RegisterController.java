package online_learn.controllers;

import online_learn.responses.ResponseBase;
import online_learn.services.register.IRegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/Register")
public class RegisterController {

    private final IRegisterService service;

    public RegisterController(IRegisterService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index() {
        ResponseBase responseBase = service.index();
        return new ModelAndView("register/index",  responseBase.getData());
    }
}
