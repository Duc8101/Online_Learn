package online_learn.controllers;

import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.home.IHomeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("Home")
public class HomeController {

    private final IHomeService service;

    public HomeController(IHomeService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index() {
        ResponseBase responseBase = service.index();
        if (responseBase.getCode() == StatusCodeConst.OK) {
            return new ModelAndView("home/index", responseBase.getData());
        }
        return new ModelAndView("shared/error", responseBase.getData());
    }
}
