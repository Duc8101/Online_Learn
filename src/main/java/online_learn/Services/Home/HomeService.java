package online_learn.Services.Home;

import online_learn.Constants.StatusCodeConst;
import online_learn.Enums.Roles;
import online_learn.Repositories.IUserRepository;
import online_learn.Responses.ResponseBase;
import online_learn.Services.Super.SuperService;
import online_learn.Views.Users.UserViewForHomePage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeService extends SuperService implements IHomeService {

    private final IUserRepository userRepository;

    public HomeService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseBase index() {
        Map<String, Object> data = new HashMap<>();
        try {
            List<UserViewForHomePage> users = userRepository.findAll().stream()
                    .filter(u -> u.getRole().getRoleId() == Roles.TEACHER.getValue())
                    .limit(4).map(u -> new UserViewForHomePage(u.getUserId(), u.getFullName(), u.getImage()))
                    .toList();

            data.put("users", users);
            data.put("code", StatusCodeConst.OK);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.OK, data);
        } catch (Exception e) {
            data.clear();
            data.put("exception", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }
}
