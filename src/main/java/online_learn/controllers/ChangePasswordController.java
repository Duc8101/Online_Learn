package online_learn.controllers;

import online_learn.services.change_password.IChangePasswordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/ChangePassword")
public class ChangePasswordController {

    private final IChangePasswordService service;

    public ChangePasswordController(IChangePasswordService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index() {
        Map<String, Object> data = service.index();
        return new ModelAndView("change_password/index", data);
    }
}
