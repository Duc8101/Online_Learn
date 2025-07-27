package online_learn.services.result;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.ResultConst;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.result_dto.ResultDetailDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.entity.Result;
import online_learn.repositories.IResultRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResultService extends BaseService implements IResultService {

    private final IResultRepository resultRepository;

    public ResultService(IResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Override
    public ResponseBase index(int quizId, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, true, true, true);
            UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");

            Result result = resultRepository.findAll().stream().filter(r -> r.getQuiz().getQuizId() == quizId
                            && r.getStudent().getUserId() == user.getUserId() && !r.getQuiz().getLesson().getCourse().isDeleted())
                    .findFirst().orElse(null);
            if (result == null) {
                data.put("error", String.format("Result not found or quiz with id = %d might be deleted or not found course", quizId));
                data.put("code",  StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            ResultDetailDTO resultDetailDTO = new ResultDetailDTO();
            resultDetailDTO.setResultId(result.getResultId());
            resultDetailDTO.setQuizId(result.getQuiz().getQuizId());
            resultDetailDTO.setStudentId(result.getStudent().getUserId());
            resultDetailDTO.setStudentName(result.getStudent().getFullName());
            resultDetailDTO.setScore(result.getScore());
            resultDetailDTO.setStatus(result.getScore() >= 5 ? ResultConst.STATUS_PASSED : ResultConst.STATUS_NOT_PASSED);

            data.put("result", resultDetailDTO);
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
