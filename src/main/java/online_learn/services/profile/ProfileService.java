package online_learn.services.profile;

import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileService extends BaseService implements IProfileService {

    @Override
    public Map<String, Object> index() {
        Map<String, Object> data = new HashMap<>();
        setValueForHeaderFooter(data, true, true, true, true);
        return data;
    }
}
