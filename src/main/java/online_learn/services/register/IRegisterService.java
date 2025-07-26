package online_learn.services.register;

import online_learn.dtos.user_dto.RegisterDTO;
import online_learn.responses.ResponseBase;

public interface IRegisterService {

    ResponseBase index();

    ResponseBase index(RegisterDTO DTO);
}
