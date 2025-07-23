package online_learn.services.forgot_password;

import online_learn.constants.StatusCodeConst;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ForgotPasswordService extends BaseService implements IForgotPasswordService {

    @Override
    public ResponseBase index() {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, false, false, true);
            return new ResponseBase(StatusCodeConst.OK, data);
        }catch(Exception e){
            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }

    @Override
    public ResponseBase index(String email) {
        return null;
    }
}
