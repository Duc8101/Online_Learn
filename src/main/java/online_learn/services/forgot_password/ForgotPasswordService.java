package online_learn.services.forgot_password;

import online_learn.constants.StatusCodeConst;
import online_learn.entity.User;
import online_learn.repositories.IUserRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import online_learn.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ForgotPasswordService extends BaseService implements IForgotPasswordService {

    private final IUserRepository userRepository;

    public ForgotPasswordService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseBase index() {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, false, false, true);
            return new ResponseBase(StatusCodeConst.OK, data);
        } catch (Exception e) {
            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }

    @Override
    public ResponseBase index(String email) {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, false, false, true);
            User user = userRepository.findAll().stream().filter(u -> u.getEmail().equalsIgnoreCase(email.trim()))
                    .findFirst().orElse(null);

            // if not found email
            if (user == null) {
                data.put("error", "Email not found");
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            String randomPw = UserUtil.randomPassword();
            String hashPw = UserUtil.hashPassword(randomPw);

            String body = UserUtil.bodyEmailForForgotPassword(randomPw);
            UserUtil.sendEmail("Welcome to Online Learn", body, email.trim());

            user.setPassword(hashPw);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            data.put("success", "Password was reset. Please check your email");
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
