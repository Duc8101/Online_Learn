package online_learn.services.forgot_password;

import online_learn.responses.ResponseBase;

public interface IForgotPasswordService {

    ResponseBase index();
    ResponseBase index(String email);
}
