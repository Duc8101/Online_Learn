package online_learn.controllers;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.user_dto.ProfileFormDTO;
import online_learn.responses.ResponseBase;
import online_learn.services.profile.IProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Profile")
public class ProfileController {

    private final IProfileService service;

    public ProfileController(IProfileService service) {
        this.service = service;
    }

    @GetMapping("")
    public ModelAndView index() {
        Map<String, Object> data = service.index();
        return new ModelAndView("profile/index", data);
    }

    @PostMapping("")
    public ModelAndView post(ProfileFormDTO DTO, HttpSession session) {
        ResponseBase responseBase = service.index(DTO, session);
        if (responseBase.getCode() == StatusCodeConst.INTERNAL_SERVER_ERROR) {
            return new ModelAndView("shared/error", responseBase.getData());
        }
        return new ModelAndView("profile/index", responseBase.getData());
    }
}
