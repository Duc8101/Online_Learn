package online_learn.services.register;

import online_learn.constants.StatusCodeConst;
import online_learn.enums.Genders;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService extends BaseService implements IRegisterService {

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
}
