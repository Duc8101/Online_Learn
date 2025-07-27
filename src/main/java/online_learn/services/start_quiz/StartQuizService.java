package online_learn.services.start_quiz;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.ResultConst;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.question_dto.QuestionListForStartQuizDTO;
import online_learn.dtos.result_dto.ResultDetailDTO;
import online_learn.dtos.start_quiz_dto.StartQuizCreateDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.entity.*;
import online_learn.enums.Answers;
import online_learn.repositories.*;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StartQuizService extends BaseService implements IStartQuizService {

    private final IQuizRepository quizRepository;
    private final IEnrollCourseRepository enrollCourseRepository;
    private final IResultRepository resultRepository;
    private final IStartQuizRepository startQuizRepository;
    private final IQuestionRepository questionRepository;
    private final IUserRepository userRepository;

    public StartQuizService(IQuizRepository quizRepository, IEnrollCourseRepository enrollCourseRepository, IResultRepository resultRepository
            , IStartQuizRepository startQuizRepository, IQuestionRepository questionRepository,  IUserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.enrollCourseRepository = enrollCourseRepository;
        this.resultRepository = resultRepository;
        this.startQuizRepository = startQuizRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    private ResultDetailDTO getResultDetailDTO(int quizId, int studentId) {
        Result result = resultRepository.findAll().stream().filter(r -> r.getQuiz().getQuizId() == quizId
                && r.getStudent().getUserId() == studentId).findFirst().orElse(null);

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
        return resultDetailDTO;
    }

    private void setAnswers(Map<String, Object> data) {
        data.put("answer1", Answers.ANSWER1);
        data.put("answer2", Answers.ANSWER2);
        data.put("answer3", Answers.ANSWER3);
        data.put("answer4", Answers.ANSWER4);
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

            ResultDetailDTO resultDetailDTO = getResultDetailDTO(quizId, user.getUserId());
            data.put("result", resultDetailDTO);
            data.put("question", question);
            data.put("quizId", quizId);
            data.put("minutes", 4);
            data.put("seconds", 59);
            data.put("question_no", 1);
            data.put("button", quiz.getQuestions().size() == 1 ? "Finish" : "Next");
            setAnswers(data);
            return new ResponseBase(StatusCodeConst.OK, data);
        } catch (Exception e) {
            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }

    @Override
    public ResponseBase next(StartQuizCreateDTO DTO, int minutes, int questionNo, int seconds, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
            User student = userRepository.findById(user.getUserId()).orElseThrow(() -> new RuntimeException("Student not found"));
            Question question = questionRepository.findById(DTO.getQuestionId()).orElse(null);
            if (question == null) {
                data.put("error", String.format("Question id = %d not found", DTO.getQuestionId()));
                data.put("code", StatusCodeConst.NOT_FOUND);
                setValueForHeaderFooter(data, true, true, true, true);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            if (question.getQuiz().getQuizId() != DTO.getQuizId()) {
                setValueForHeaderFooter(data, true, true, true, true);
                data.put("error", "Question doesn't belong to this quiz");
                data.put("code", StatusCodeConst.BAD_REQUEST);
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            setValueForHeaderFooter(data, false, true, false, false);

            StartQuiz startQuiz = startQuizRepository.findAll().stream().filter(sq -> sq.getQuestion().getQuestionId() == DTO.getQuestionId()
                    && sq.getStudent().getUserId() == user.getUserId()).findFirst().orElse(null);

            // if not answer current question yet
            if (startQuiz == null) {
                startQuiz = new StartQuiz();
                startQuiz.setQuestion(question);
                startQuiz.setStudent(student);
                startQuiz.setAnswer(DTO.getAnswer());
                startQuizRepository.save(startQuiz);
            } else {
                startQuiz.setAnswer(DTO.getAnswer());
                startQuizRepository.save(startQuiz);
            }

            // -------------- get next question ---------------
            List<Question> questions = questionRepository.findAll().stream().filter(q -> q.getQuiz().getQuizId() == DTO.getQuizId())
                    .toList();

            QuestionListForStartQuizDTO questionListForStartQuizDTO = new QuestionListForStartQuizDTO();
            questionListForStartQuizDTO.setQuestionId(questions.get(questionNo).getQuestionId());
            questionListForStartQuizDTO.setQuestionName(questions.get(questionNo).getQuestionName());
            questionListForStartQuizDTO.setQuizId(DTO.getQuizId());
            questionListForStartQuizDTO.setAnswer1(questions.get(questionNo).getAnswer1());
            questionListForStartQuizDTO.setAnswer2(questions.get(questionNo).getAnswer2());
            questionListForStartQuizDTO.setAnswer3(questions.get(questionNo).getAnswer3());
            questionListForStartQuizDTO.setAnswer4(questions.get(questionNo).getAnswer4());

            startQuiz = startQuizRepository.findAll().stream().filter(sq -> sq.getQuestion().getQuestionId() == questions.get(questionNo).getQuestionId()
                    && sq.getStudent().getUserId() == user.getUserId()).findFirst().orElse(null);

            // if not answer next question yet
            if (startQuiz == null) {
                questionListForStartQuizDTO.setChosenAnswer(null);
            } else {
                questionListForStartQuizDTO.setChosenAnswer(startQuiz.getAnswer());
            }

            ResultDetailDTO resultDetailDTO = getResultDetailDTO(DTO.getQuizId(), user.getUserId());

            data.put("result", resultDetailDTO);
            data.put("question", questionListForStartQuizDTO);
            data.put("quizId", DTO.getQuizId());
            data.put("minutes", minutes);
            data.put("seconds", seconds);
            data.put("question_no", questionNo + 1);
            data.put("button", questions.size() == questionNo + 1 ? "Finish" : "Next");
            setAnswers(data);
            return new ResponseBase(StatusCodeConst.OK, data);
        } catch (Exception e) {
            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }

    @Override
    public ResponseBase previous(StartQuizCreateDTO DTO, int minutes, int questionNo, int seconds, HttpSession session) {
        return null;
    }

    @Override
    public ResponseBase finish(StartQuizCreateDTO DTO, HttpSession session) {
        return null;
    }
}
