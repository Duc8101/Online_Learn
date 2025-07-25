package online_learn.services.change_password;

import online_learn.dtos.user_dto.ChangePasswordDTO;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChangePasswordService extends BaseService implements IChangePasswordService {

    @Override
    public Map<String, Object> index() {
        Map<String, Object> data = new HashMap<>();
        setValueForHeaderFooter(data, true, true, true, true);
        return data;
    }

    @Override
    public ResponseBase index(ChangePasswordDTO DTO) {
        return null;
    }
}
