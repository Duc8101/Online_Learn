package online_learn.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.login.ILoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/Login")
public class LoginController {

    private final ILoginService service;

    public LoginController(ILoginService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index(HttpServletRequest request) {
        ResponseBase responseBase = service.index(request);
        if (responseBase.getCode() == StatusCodeConst.OK) {
            if (responseBase.getData().isEmpty()) {
                return new ModelAndView("redirect:/Home");
            }
            return new ModelAndView("login/index", responseBase.getData());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }

    @PostMapping("")
    public ModelAndView index(String username, String password, HttpSession session, HttpServletResponse response) {
        ResponseBase responseBase = service.index(username, password, session, response);
        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("redirect:/Home");
        }

        if (responseBase.getCode() == StatusCodeConst.INTERNAL_SERVER_ERROR) {
            return new ModelAndView("shared/error", responseBase.getData());
        }

        return new ModelAndView("login/index", responseBase.getData());
    }
}
