package online_learn.services.change_password;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.user_dto.ChangePasswordDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.entity.User;
import online_learn.repositories.IUserRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import online_learn.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChangePasswordService extends BaseService implements IChangePasswordService {

    private final IUserRepository userRepository;

    public ChangePasswordService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> index() {
        Map<String, Object> data = new HashMap<>();
        setValueForHeaderFooter(data, true, true, true, true);
        return data;
    }

    @Override
    public ResponseBase index(ChangePasswordDTO DTO, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, true, true, true);
            UserProfileInfoDTO userProfileInfoDTO = (UserProfileInfoDTO) session.getAttribute("user");
            User user = userRepository.findById(userProfileInfoDTO.getUserId()).orElseThrow(() ->
                    new RuntimeException("User not found"));

            if (!user.getPassword().equals(UserUtil.hashPassword(DTO.getCurrentPassword()))) {
                data.put("error", "Current password not correct");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            user.setPassword(UserUtil.hashPassword(DTO.getNewPassword()));
            userRepository.save(user);

            data.put("success", "Update successful");
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
