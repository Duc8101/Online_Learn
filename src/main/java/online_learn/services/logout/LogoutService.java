package online_learn.services.logout;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LogoutService extends BaseService implements ILogoutService {

    @Override
    public ResponseBase index(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<>();
        try {
            request.getSession().invalidate();
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
            return new ResponseBase(StatusCodeConst.OK, data);
        }catch (Exception e){
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }
}
