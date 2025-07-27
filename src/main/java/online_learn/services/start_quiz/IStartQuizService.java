package online_learn.services.start_quiz;

import jakarta.servlet.http.HttpSession;
import online_learn.responses.ResponseBase;

public interface IStartQuizService {

    ResponseBase index(int quizId, HttpSession session);
}
