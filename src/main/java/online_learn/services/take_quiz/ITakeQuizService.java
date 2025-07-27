package online_learn.services.take_quiz;

import online_learn.responses.ResponseBase;

public interface ITakeQuizService {

    ResponseBase index(int lessonId);
}
