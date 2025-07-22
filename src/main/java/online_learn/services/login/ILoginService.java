package online_learn.services.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import online_learn.responses.ResponseBase;

public interface ILoginService {

    ResponseBase index(HttpServletRequest request);
    ResponseBase index(String username, String password, HttpSession session, HttpServletResponse response);
}
