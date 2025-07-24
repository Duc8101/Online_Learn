package online_learn.services.profile;

import online_learn.enums.Genders;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfileService extends BaseService implements IProfileService {

    @Override
    public Map<String, Object> index() {
        Map<String, Object> data = new HashMap<>();
        setValueForHeaderFooter(data, true, true, true, true);
        List<Genders> genders = Arrays.asList(Genders.values());
        data.put("genders", genders);
        return data;
    }
}
