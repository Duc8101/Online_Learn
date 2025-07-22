package online_learn.services.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.constants.UserConst;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.entity.User;
import online_learn.repositories.IUserRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import online_learn.utils.ParseUtil;
import online_learn.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService extends BaseService implements ILoginService {

    private final IUserRepository userRepository;

    public LoginService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseBase index(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        try {
            // get all cookies
            Cookie[] cookies = request.getCookies();

            String UserId = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    UserId = cookie.getValue();
                    break;
                }
            }

            if (UserId == null) {
                setValueForHeaderFooter(data, true, false, false, true);
                return new ResponseBase(StatusCodeConst.OK, data);
            }

            if (!ParseUtil.intTryParse(UserId)) {
                data.put("error", "UserId is invalid");
                data.put("code", StatusCodeConst.BAD_REQUEST);
                setValueForHeaderFooter(data, true, true, true, true);
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            int userId = Integer.parseInt(UserId);
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                data.put("error", String.format("User not found with id %d", userId));
                data.put("code", StatusCodeConst.NOT_FOUND);
                setValueForHeaderFooter(data, true, true, true, true);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            UserProfileInfoDTO userProfileInfoDTO = getUserProfileInfo(user);
            HttpSession session = request.getSession();
            session.setAttribute("user", userProfileInfoDTO);
            return new ResponseBase(StatusCodeConst.OK, data);
        } catch (Exception e) {
            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }

    private UserProfileInfoDTO getUserProfileInfo(User user) {
        UserProfileInfoDTO userProfileInfoDTO = new UserProfileInfoDTO();
        userProfileInfoDTO.setUserId(user.getUserId());
        userProfileInfoDTO.setEmail(user.getEmail());
        userProfileInfoDTO.setAddress(user.getAddress());
        userProfileInfoDTO.setPhone(user.getPhone());
        userProfileInfoDTO.setFullName(user.getFullName());
        userProfileInfoDTO.setGender(user.getGender());
        userProfileInfoDTO.setUsername(user.getUsername());
        userProfileInfoDTO.setRoleId(user.getRole().getRoleId());
        userProfileInfoDTO.setImage(user.getImage());
        return userProfileInfoDTO;
    }

    @Override
    public ResponseBase index(String username, String password, HttpSession session, HttpServletResponse response) {
        Map<String, Object> data = new HashMap<>();
        try {
            User user = userRepository.findAll().stream().filter(u -> u.getUsername().equalsIgnoreCase(username))
                    .findFirst().orElse(null);
            // if username not exist
            if (user == null) {
                setValueForHeaderFooter(data, true, false, false, true);
                data.put("message", "Username not found");
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            // if wrong password
            if (!user.getPassword().equals(UserUtil.hashPassword(password))) {
                setValueForHeaderFooter(data, true, false, false, true);
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                userRepository.save(user);

                // if not failed enough 5 times
                if (user.getFailedLoginAttempts() < UserConst.MAX_FAILED_LOGIN_ATTEMPTS) {
                    int numberTimes = UserConst.MAX_FAILED_LOGIN_ATTEMPTS - user.getFailedLoginAttempts();
                    String strTimes = UserConst.MAX_FAILED_LOGIN_ATTEMPTS - user.getFailedLoginAttempts() == 1 ? "time" : "times";
                    data.put("message", String.format("Incorrect password. You have more %d %s", numberTimes, strTimes));
                    return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
                }

                LocalDateTime lockoutEndTime;
                // if failed enough 5 times
                if (user.getLockoutEndTime() == null)
                {
                    user.setLockoutEndTime(LocalDateTime.now().plusMinutes(15));;
                    userRepository.save(user);
                }
                lockoutEndTime = user.getLockoutEndTime();

                int numberMinutes = lockoutEndTime.getMinute() - LocalDateTime.now().getMinute();
                String strMinutes = lockoutEndTime.getMinute() - LocalDateTime.now().getMinute() == 1 ? "minute" : "minutes";
                data.put("message", String.format("You have failed to login 5 times. Please come back after %d %s", numberMinutes, strMinutes));
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            // if true password but lockoutTime not end
            if (user.getLockoutEndTime() != null && user.getLockoutEndTime().isAfter(LocalDateTime.now()))
            {
                setValueForHeaderFooter(data, true, false, false, true);
                int numberMinutes = user.getLockoutEndTime().getMinute() - LocalDateTime.now().getMinute();
                String strMinutes = user.getLockoutEndTime().getMinute() - LocalDateTime.now().getMinute() == 1 ? "minute" : "minutes";
                data.put("message", String.format("Please come back after %d %s", numberMinutes, strMinutes));
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            // if true password and lockoutTime end
            if (user.getFailedLoginAttempts() > 0)
            {
                user.setFailedLoginAttempts(0);
                user.setLockoutEndTime(null);
                userRepository.save(user);
            }

            UserProfileInfoDTO userProfileInfoDTO = getUserProfileInfo(user);
            session.setAttribute("user", userProfileInfoDTO);

            Cookie cookie = new Cookie("userId", String.valueOf(user.getUserId()));
            response.addCookie(cookie);
            return new ResponseBase(StatusCodeConst.OK, data);
        } catch (Exception e) {
            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }
}
