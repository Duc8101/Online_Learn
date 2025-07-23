package online_learn.services.register;

import online_learn.constants.StatusCodeConst;
import online_learn.dtos.user_dto.RegisterDTO;
import online_learn.entity.Role;
import online_learn.entity.User;
import online_learn.enums.Genders;
import online_learn.enums.Roles;
import online_learn.repositories.IRoleRepository;
import online_learn.repositories.IUserRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import online_learn.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService extends BaseService implements IRegisterService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    public RegisterService(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseBase index() {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, false, false, true);
            data.put("male", Genders.MALE);
            data.put("female", Genders.FEMALE);
            data.put("other", Genders.OTHER);
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
    public ResponseBase index(RegisterDTO DTO) {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, false, false, true);
            data.put("male", Genders.MALE);
            data.put("female", Genders.FEMALE);
            data.put("other", Genders.OTHER);

            if (userRepository.findAll().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(DTO.getUsername())
                    || u.getEmail().equalsIgnoreCase(DTO.getEmail().trim()))) {
                data.put("error", "Username or email already exists");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            String newPw = UserUtil.randomPassword();
            String hashPw = UserUtil.hashPassword(newPw);

            User user = new User();
            user.setFullName(DTO.getFullName().trim());
            user.setEmail(DTO.getEmail().trim());
            user.setPassword(hashPw);
            user.setUsername(DTO.getUsername());
            user.setGender(DTO.getGender());
            user.setPhone(DTO.getPhone());
            user.setAddress(DTO.getAddress() == null || DTO.getAddress().trim().isEmpty() ? null : DTO.getAddress().trim());
            user.setImage("https://i.pinimg.com/564x/31/ec/2c/31ec2ce212492e600b8de27f38846ed7.jpg");

            Role role = roleRepository.findById(Roles.STUDENT.getValue()).orElse(null);

            String body = UserUtil.bodyEmailForRegister(newPw);
            UserUtil.sendEmail("Welcome to Online Learn", body, DTO.getEmail().trim());

            user.setRole(role);
            user.setFailedLoginAttempts(0);
            user.setLockoutEndTime(null);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            data.put("success", "Register successful");
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
