package online_learn.interceptors;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import online_learn.constants.StatusCodeConst;
import online_learn.constants.UserConst;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class GuestOrStudentInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        UserProfileInfoDTO user = (UserProfileInfoDTO) request.getSession().getAttribute("user");
        // login as admin or teacher
        if (!(user == null || user.getRoleId() == UserConst.ROLE_STUDENT)) {
            request.setAttribute(RequestDispatcher.ERROR_MESSAGE, "You are not allowed to access");
            request.getRequestDispatcher(String.format("/error/%d", StatusCodeConst.FORBIDDEN)).forward(request, response);
            return false;
        }
        return true;
    }
}
