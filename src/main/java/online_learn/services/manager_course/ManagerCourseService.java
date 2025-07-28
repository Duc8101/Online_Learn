package online_learn.services.manager_course;

import jakarta.servlet.http.HttpSession;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public class ManagerCourseService extends BaseService implements IManagerCourseService {

    @Override
    public ResponseBase list(String page, HttpSession session) {
        return null;
    }
}
