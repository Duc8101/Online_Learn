package online_learn.services.error;

import online_learn.constants.StatusCodeConst;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ErrorService extends BaseService implements IErrorService {

    @Override
    public Map<String, Object> index(int statusCode) {
        Map<String, Object> data = new HashMap<>();
        setValueForHeaderFooter(data, true, true, true, true);

        if (statusCode == StatusCodeConst.UNAUTHORIZED) {
            data.put("code", StatusCodeConst.UNAUTHORIZED);
            data.put("error", "Authentication Failed");
        }
        return data;
    }
}
