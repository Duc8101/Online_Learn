package online_learn.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.logout.ILogoutService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/Logout")
public class LogoutController {

    private final ILogoutService service;

    public LogoutController(ILogoutService service) {
        this.service = service;
    }

    @GetMapping
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        ResponseBase responseBase = service.index(request, response);
        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("redirect:/Home");
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }
}
