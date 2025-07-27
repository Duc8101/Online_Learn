package online_learn.services.start_quiz;

import jakarta.servlet.http.HttpSession;
import online_learn.dtos.start_quiz_dto.StartQuizCreateDTO;
import online_learn.responses.ResponseBase;

public interface IStartQuizService {

    ResponseBase index(int quizId, HttpSession session);

    ResponseBase next(StartQuizCreateDTO DTO, int minutes, int questionNo, int seconds, HttpSession session);

    ResponseBase previous(StartQuizCreateDTO DTO, int minutes, int questionNo, int seconds, HttpSession session);

    ResponseBase finish(StartQuizCreateDTO DTO, HttpSession session);
}
