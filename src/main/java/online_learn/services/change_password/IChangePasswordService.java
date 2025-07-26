package online_learn.services.change_password;

import jakarta.servlet.http.HttpSession;
import online_learn.dtos.user_dto.ChangePasswordDTO;
import online_learn.responses.ResponseBase;

import java.util.Map;

public interface IChangePasswordService {

    Map<String, Object> index();

    ResponseBase index(ChangePasswordDTO DTO, HttpSession session);
}
