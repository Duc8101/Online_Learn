package online_learn.services.home;

import online_learn.constants.StatusCodeConst;
import online_learn.enums.Roles;
import online_learn.repositories.IUserRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import online_learn.dtos.user_dto.UserInfoForHomePageDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeService extends BaseService implements IHomeService {

    private final IUserRepository userRepository;

    public HomeService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseBase index() {
        Map<String, Object> data = new HashMap<>();
        try {
            List<UserInfoForHomePageDTO> users = userRepository.findAll().stream()
                    .filter(u -> u.getRole().getRoleId() == Roles.TEACHER.getValue())
                    .limit(4).map(u -> new UserInfoForHomePageDTO(u.getUserId(), u.getFullName(), u.getImage()))
                    .toList();

            data.put("users", users);
            data.put("code", StatusCodeConst.OK);
            setValueForHeaderFooter(data, true, true, true, true);
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
