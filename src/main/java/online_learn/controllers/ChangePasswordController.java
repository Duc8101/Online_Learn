package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.user_dto.ChangePasswordDTO;
import online_learn.responses.ResponseBase;
import online_learn.services.change_password.IChangePasswordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("")
    public ModelAndView index(ChangePasswordDTO DTO, HttpSession session) {
        ResponseBase responseBase = service.index(DTO, session);
        if (responseBase.getCode() == StatusCodeConst.INTERNAL_SERVER_ERROR) {
            return new ModelAndView("shared/error", responseBase.getData());
        }
        return new ModelAndView("change_password/index", responseBase.getData());
    }
}
