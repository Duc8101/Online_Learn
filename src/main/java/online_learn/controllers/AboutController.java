package online_learn.controllers;

import online_learn.services.about.IAboutService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("About")
public class AboutController {

    private final IAboutService service;

    public AboutController(IAboutService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index() {
        Map<String, Object> data = service.index();
        return new ModelAndView("about/index", data);
    }
}
