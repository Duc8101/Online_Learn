package online_learn.controllers;

import online_learn.services.error.IErrorService;
import online_learn.utils.ParseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/Error")
public class ErrorController {

    private final IErrorService service;

    public ErrorController(IErrorService service) {
        this.service = service;
    }

    @GetMapping("/{statusCode}")
    public ModelAndView index(@PathVariable(required = false, name = "statusCode") String statusCode) {
        if (ParseUtil.intTryParse(statusCode)) {
            Map<String, Object> data = service.index(Integer.parseInt(statusCode));
            return new ModelAndView("shared/error", data);
        }
        return new ModelAndView("redirect:/Home");
    }
}
