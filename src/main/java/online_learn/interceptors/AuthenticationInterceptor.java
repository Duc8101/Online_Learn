package online_learn.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import online_learn.constants.StatusCodeConst;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // if not login
        if (request.getSession().getAttribute("user") == null) {
            request.getRequestDispatcher(String.format("/Error/%d", StatusCodeConst.UNAUTHORIZED)).forward(request, response);
            return false;
        }
        return true;
    }

}
