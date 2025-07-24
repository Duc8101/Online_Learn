package online_learn.services.error;

import java.util.Map;

public interface IErrorService {

    Map<String, Object> index(int statusCode);
}
