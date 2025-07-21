package online_learn.controllers;

import online_learn.services.faq.IFAQService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("FAQ")
public class FAQController {

    private final IFAQService service;

    public FAQController(IFAQService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index() {
        Map<String, Object> data = service.index();
        return new ModelAndView("faq/index", data);
    }
}
