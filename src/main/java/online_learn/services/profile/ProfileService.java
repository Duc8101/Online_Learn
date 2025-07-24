package online_learn.services.profile;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.user_dto.ProfileFormDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.entity.User;
import online_learn.enums.Genders;
import online_learn.repositories.IUserRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ProfileService extends BaseService implements IProfileService {

    private final IUserRepository userRepository;

    public ProfileService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> index() {
        Map<String, Object> data = new HashMap<>();
        setValueForHeaderFooter(data, true, true, true, true);
        List<Genders> genders = Arrays.asList(Genders.values());
        data.put("genders", genders);
        return data;
    }

    @Override
    public ResponseBase index(ProfileFormDTO DTO, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, true, true, true);
            List<Genders> genders = Arrays.asList(Genders.values());
            data.put("genders", genders);
            UserProfileInfoDTO userProfileInfoDTO = (UserProfileInfoDTO) session.getAttribute("user");
            User user = userRepository.findById(userProfileInfoDTO.getUserId()).orElseThrow(() ->
                    new RuntimeException("User not found"));

            if (DTO.getFullName().trim().isEmpty()) {
                data.put("error", "You have to input your name");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            if (DTO.getPhone() != null && !DTO.getPhone().isEmpty() && DTO.getPhone().length() != 10) {
                data.put("error", "Phone must be 10 numbers");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            String patternEmail = "^[a-zA-Z][\\w-]+@([\\w]+.[\\w]+|[\\w]+.[\\w]{2,}.[\\w]{2,})";
            if (!Pattern.matches(patternEmail, DTO.getEmail().trim())) {
                data.put("error", "Email invalid");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            if (DTO.getAddress() != null && !DTO.getAddress().trim().isEmpty() && DTO.getAddress().trim().length() > 100) {
                data.put("error", "Address maximum 100 characters");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(DTO.getEmail().trim())
                    && u.getUserId() != user.getUserId())) {
                data.put("error", "Email already exists");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            userProfileInfoDTO.setFullName(DTO.getFullName().trim());
            userProfileInfoDTO.setPhone(DTO.getPhone() == null || DTO.getPhone().isEmpty() ? "" : DTO.getPhone().trim());
            userProfileInfoDTO.setEmail(DTO.getEmail().trim());
            userProfileInfoDTO.setAddress(DTO.getAddress() == null ||  DTO.getAddress().trim().isEmpty() ? null : DTO.getAddress().trim());
            userProfileInfoDTO.setGender(DTO.getGender());

            if (!DTO.getImage().trim().isEmpty()) {
                user.setImage(String.format("/img/%s",  DTO.getImage().trim()));
                userProfileInfoDTO.setImage(String.format("/img/%s",  DTO.getImage().trim()));
            }

            user.setFullName(DTO.getFullName().trim());
            user.setPhone(DTO.getPhone() == null || DTO.getPhone().isEmpty() ? "" : DTO.getPhone().trim());
            user.setEmail(DTO.getEmail().trim());
            user.setAddress(DTO.getAddress() == null ||  DTO.getAddress().trim().isEmpty() ? null : DTO.getAddress().trim());
            user.setGender(DTO.getGender());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            session.setAttribute("user", userProfileInfoDTO);
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
