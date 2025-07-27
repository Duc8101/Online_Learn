package online_learn.services.take_quiz;

import online_learn.constants.StatusCodeConst;
import online_learn.dtos.quiz_dto.QuizListDTO;
import online_learn.entity.Lesson;
import online_learn.repositories.ILessonRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TakeQuizService extends BaseService implements ITakeQuizService {

    private final ILessonRepository lessonRepository;

    public TakeQuizService(ILessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public ResponseBase index(int lessonId) {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, true, true, true);
            Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
            if (lesson == null) {
                data.put("error", String.format("Lesson with id %d does not exist", lessonId));
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            if (lesson.getQuizzes().isEmpty()) {
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            List<QuizListDTO> quizzes = lesson.getQuizzes().stream().map(q -> new QuizListDTO(q.getQuizId()
                    , q.getQuizName(), q.getLesson().getLessonId(), !q.getQuestions().isEmpty())).toList();
            data.put("quizzes", quizzes);
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
