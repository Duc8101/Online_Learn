package online_learn.services.result;

import jakarta.servlet.http.HttpSession;
import online_learn.responses.ResponseBase;

public interface IResultService {

    ResponseBase index(int quizId, HttpSession session);
}
