package online_learn.services.logout;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online_learn.responses.ResponseBase;

public interface ILogoutService {

    ResponseBase index(HttpServletRequest request, HttpServletResponse response);
}
