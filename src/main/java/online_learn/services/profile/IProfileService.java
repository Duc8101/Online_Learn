package online_learn.services.profile;

import jakarta.servlet.http.HttpSession;
import online_learn.dtos.user_dto.ProfileFormDTO;
import online_learn.responses.ResponseBase;

import java.util.Map;

public interface IProfileService {

    Map<String, Object> index();
    ResponseBase index(ProfileFormDTO DTO, HttpSession session);
}
