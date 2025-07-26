package online_learn.services.forgot_password;

import online_learn.responses.ResponseBase;

import java.util.Map;

public interface IForgotPasswordService {

    Map<String, Object> index();

    ResponseBase index(String email);
}
