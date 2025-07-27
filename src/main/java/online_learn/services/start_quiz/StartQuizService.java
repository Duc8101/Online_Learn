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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Comparator;
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
            , IStartQuizRepository startQuizRepository, IQuestionRepository questionRepository, IUserRepository userRepository) {
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
            data.put("minutes", 0);
            data.put("seconds", 20);
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
            ResponseBase responseBase = setDataForNextAndPreviousAndFinish(data, DTO, session);
            if (responseBase.getCode() == StatusCodeConst.OK) {

                // -------------- set next question ---------------
                List<Question> questions = questionRepository.findAll().stream().filter(q -> q.getQuiz().getQuizId() == DTO.getQuizId())
                        .toList();

                QuestionListForStartQuizDTO questionListForStartQuizDTO = getQuestionListForStartQuizDTO(questions, questionNo, DTO.getQuizId());

                setDataForNextAndPrevious(data, session, questionListForStartQuizDTO, DTO.getQuizId(), questions.get(questionNo).getQuestionId()
                        , minutes, seconds);
                data.put("question_no", questionNo + 1);
                data.put("button", questions.size() == questionNo + 1 ? "Finish" : "Next");
                setAnswers(data);
            }
            return responseBase;
        } catch (Exception e) {
            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }

    private ResponseBase setDataForNextAndPreviousAndFinish(Map<String, Object> data, StartQuizCreateDTO DTO, HttpSession session) {
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
        return new ResponseBase(StatusCodeConst.OK, data);
    }

    private void setDataForNextAndPrevious(Map<String, Object> data, HttpSession session, QuestionListForStartQuizDTO questionListForStartQuizDTO, int quizId, int questionId, int minutes, int seconds) {
        UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
        StartQuiz startQuiz = startQuizRepository.findAll().stream().filter(sq -> sq.getQuestion().getQuestionId() == questionId
                && sq.getStudent().getUserId() == user.getUserId()).findFirst().orElse(null);

        // if not answer previous question yet
        if (startQuiz == null) {
            questionListForStartQuizDTO.setChosenAnswer(null);
        } else {
            questionListForStartQuizDTO.setChosenAnswer(startQuiz.getAnswer());
        }

        ResultDetailDTO resultDetailDTO = getResultDetailDTO(quizId, user.getUserId());

        data.put("result", resultDetailDTO);
        data.put("question", questionListForStartQuizDTO);
        data.put("quizId", quizId);
        data.put("minutes", minutes);
        data.put("seconds", seconds);
    }

    private QuestionListForStartQuizDTO getQuestionListForStartQuizDTO(List<Question> questions, int index, int quizId) {
        QuestionListForStartQuizDTO questionListForStartQuizDTO = new QuestionListForStartQuizDTO();
        questionListForStartQuizDTO.setQuestionId(questions.get(index).getQuestionId());
        questionListForStartQuizDTO.setQuestionName(questions.get(index).getQuestionName());
        questionListForStartQuizDTO.setQuizId(quizId);
        questionListForStartQuizDTO.setAnswer1(questions.get(index).getAnswer1());
        questionListForStartQuizDTO.setAnswer2(questions.get(index).getAnswer2());
        questionListForStartQuizDTO.setAnswer3(questions.get(index).getAnswer3());
        questionListForStartQuizDTO.setAnswer4(questions.get(index).getAnswer4());
        return questionListForStartQuizDTO;
    }

    @Override
    public ResponseBase previous(StartQuizCreateDTO DTO, int minutes, int questionNo, int seconds, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            ResponseBase responseBase = setDataForNextAndPreviousAndFinish(data, DTO, session);
            if (responseBase.getCode() == StatusCodeConst.OK) {

                // -------------- set previous question ---------------
                List<Question> questions = questionRepository.findAll().stream().filter(q -> q.getQuiz().getQuizId() == DTO.getQuizId())
                        .toList();

                QuestionListForStartQuizDTO questionListForStartQuizDTO = getQuestionListForStartQuizDTO(questions, questionNo - 2, DTO.getQuizId());

                setDataForNextAndPrevious(data, session, questionListForStartQuizDTO, DTO.getQuizId(), questions.get(questionNo - 2).getQuestionId()
                        , minutes, seconds);

                data.put("question_no", questionNo - 1);
                data.put("button", "Next");
                setAnswers(data);
            }
            return responseBase;
        } catch (Exception e) {
            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }

    @Override
    @Transactional
    public ResponseBase finish(StartQuizCreateDTO DTO, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            ResponseBase responseBase = setDataForNextAndPreviousAndFinish(data, DTO, session);
            if (responseBase.getCode() == StatusCodeConst.OK) {
                UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
                User student = userRepository.findById(user.getUserId()).orElseThrow(() -> new RuntimeException("Student not found"));

                // get all start quizzes
                List<StartQuiz> startQuizzes = startQuizRepository.findAll().stream().filter(sq -> sq.getQuestion().getQuiz().getQuizId() == DTO.getQuizId()
                        && sq.getStudent().getUserId() == user.getUserId()).sorted(Comparator.comparing(s -> s.getQuestion().getQuestionId()))
                        .toList();

                // get questions
                List<Question> questions = questionRepository.findAll().stream().filter(q -> q.getQuiz().getQuizId() == DTO.getQuizId()
                        && startQuizzes.stream().anyMatch(s -> s.getQuestion().getQuestionId() == q.getQuestionId()))
                        .toList();

                long numberQuestions = questionRepository.findAll().stream().filter(q -> q.getQuiz().getQuizId() == DTO.getQuizId()).count();
                int score = 0;

                for (int i = 0; i < questions.size(); i++)
                {
                    if (startQuizzes.get(i).getAnswer() == questions.get(i).getAnswerCorrect())
                    {
                        score++;
                    }
                }

                double finalScore = (double) score / numberQuestions * 10;
                Result result = resultRepository.findAll().stream().filter(r -> r.getQuiz().getQuizId() == DTO.getQuizId()
                        && r.getStudent().getUserId() == user.getUserId()).findFirst().orElse(null);

                if (result == null) {
                    Quiz quiz = quizRepository.findById(DTO.getQuizId()).orElseThrow(() -> new RuntimeException("Quiz not found"));

                    result = new Result();
                    result.setQuiz(quiz);
                    result.setScore(finalScore);
                    result.setStudent(student);
                    resultRepository.save(result);
                } else {
                    result.setScore(finalScore);
                    resultRepository.save(result);
                }

                //startQuizRepository.deleteAll(startQuizzes);
            }
            return responseBase;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }
}
