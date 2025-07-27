package online_learn.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error/{statusCode}")
    public ModelAndView handleError(@PathVariable int statusCode, HttpServletRequest request) {
        String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Map<String, Object> data = new HashMap<>();
        data.put("code", statusCode);
        data.put("error", errorMessage);
        return new ModelAndView("shared/error", data);
    }
}
