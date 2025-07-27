package online_learn.services.start_quiz;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.ResultConst;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.question_dto.QuestionListForStartQuizDTO;
import online_learn.dtos.result_dto.ResultDetailDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.entity.Quiz;
import online_learn.entity.Result;
import online_learn.entity.StartQuiz;
import online_learn.enums.Answers;
import online_learn.repositories.IEnrollCourseRepository;
import online_learn.repositories.IQuizRepository;
import online_learn.repositories.IResultRepository;
import online_learn.repositories.IStartQuizRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StartQuizService extends BaseService implements IStartQuizService {

    private final IQuizRepository quizRepository;
    private final IEnrollCourseRepository enrollCourseRepository;
    private final IResultRepository resultRepository;
    private final IStartQuizRepository startQuizRepository;

    public StartQuizService(IQuizRepository quizRepository, IEnrollCourseRepository enrollCourseRepository, IResultRepository resultRepository
            , IStartQuizRepository startQuizRepository) {
        this.quizRepository = quizRepository;
        this.enrollCourseRepository = enrollCourseRepository;
        this.resultRepository = resultRepository;
        this.startQuizRepository = startQuizRepository;
    }

    @Override
    public ResponseBase index(int quizId, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
            Quiz quiz = quizRepository.findById(quizId).orElse(null);

            if (quiz == null) {
                data.put("error", String.format("Quiz id = %d not found or course might be deleted", quizId));
                data.put("code", StatusCodeConst.NOT_FOUND);
                setValueForHeaderFooter(data, true, true, true, true);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            // if not enroll course
            if (enrollCourseRepository.findAll().stream().noneMatch(ec -> ec.getCourse().getCourseId() == quiz.getLesson().getCourse().getCourseId()
                    && ec.getStudent().getUserId() == user.getUserId())) {
                setValueForHeaderFooter(data, true, true, true, true);
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            // if not exist question
            if (quiz.getQuestions().isEmpty()) {
                data.put("lessonId", quiz.getLesson().getLessonId());
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            setValueForHeaderFooter(data, false, true, false, false);

            // delete start quiz of student
            List<StartQuiz> startQuizzes = startQuizRepository.findAll().stream().filter(sq -> sq.getStudent().getUserId() == user.getUserId())
                    .toList();
            startQuizRepository.deleteAll(startQuizzes);

            // set question
            QuestionListForStartQuizDTO question = new QuestionListForStartQuizDTO();
            question.setQuestionId(quiz.getQuestions().get(0).getQuestionId());
            question.setQuestionName(quiz.getQuestions().get(0).getQuestionName());
            question.setQuizId(quizId);
            question.setAnswer1(quiz.getQuestions().get(0).getAnswer1());
            question.setAnswer2(quiz.getQuestions().get(0).getAnswer2());
            question.setAnswer3(quiz.getQuestions().get(0).getAnswer3());
            question.setAnswer4(quiz.getQuestions().get(0).getAnswer4());
            question.setChosenAnswer(null);

            Result result = resultRepository.findAll().stream().filter(r -> r.getQuiz().getQuizId() == quizId
                    && r.getStudent().getUserId() == user.getUserId()).findFirst().orElse(null);

            ResultDetailDTO resultDetailDTO;
            if (result == null) {
                resultDetailDTO = null;
            } else {
                resultDetailDTO = new ResultDetailDTO();
                resultDetailDTO.setResultId(result.getResultId());
                resultDetailDTO.setQuizId(result.getQuiz().getQuizId());
                resultDetailDTO.setStudentId(result.getStudent().getUserId());
                resultDetailDTO.setStudentName(result.getStudent().getFullName());
                resultDetailDTO.setScore(result.getScore());
                resultDetailDTO.setStatus(result.getScore() >= 5 ? ResultConst.STATUS_PASSED : ResultConst.STATUS_NOT_PASSED);
            }

            data.put("result", resultDetailDTO);
            data.put("question", question);
            data.put("quizId", quizId);
            data.put("minutes", 4);
            data.put("seconds", 59);
            data.put("question_no", 1);
            data.put("button", quiz.getQuestions().size() == 1 ? "Finish" : "Next");
            data.put("answer1", Answers.ANSWER1);
            data.put("answer2", Answers.ANSWER2);
            data.put("answer3", Answers.ANSWER3);
            data.put("answer4", Answers.ANSWER4);
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
