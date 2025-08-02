package online_learn.services.manager_lesson;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.lesson_dto.LessonListForCourseDetailOrViewLessonDTO;
import online_learn.dtos.pdf_dto.PdfListDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.dtos.video_dto.VideoListDTO;
import online_learn.entity.Course;
import online_learn.entity.Lesson;
import online_learn.entity.Video;
import online_learn.repositories.ICourseRepository;
import online_learn.repositories.IPdfRepository;
import online_learn.repositories.IVideoRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerLessonService extends BaseService implements IManagerLessonService {

    private final ICourseRepository courseRepository;
    private final IVideoRepository videoRepository;
    private final IPdfRepository pdfRepository;

    public ManagerLessonService(ICourseRepository courseRepository,  IVideoRepository videoRepository, IPdfRepository pdfRepository) {
        this.courseRepository = courseRepository;
        this.videoRepository = videoRepository;
        this.pdfRepository = pdfRepository;
    }

    @Override
    public ResponseBase list(int courseId, String video, String name, String pdf, Integer lessonId, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
            Course course = courseRepository.findAll().stream().filter(c -> c.getCourseId() == courseId
                    && !c.isDeleted() && c.getCreator().getUserId() == user.getUserId()).findFirst().orElse(null);

            if (course == null) {
                setValueForHeaderFooter(data, true, true, true, true);
                data.put("error", String.format("Course with id = %d does not exist", courseId));
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            setValueForHeaderFooter(data, false, true, false, false);

            List<LessonListForCourseDetailOrViewLessonDTO> lessonListDTOs = course.getLessons().stream()
                    .sorted(Comparator.comparing(Lesson::getCreatedAt)).map(l -> new LessonListForCourseDetailOrViewLessonDTO(
                            l.getLessonId(), l.getLessonName(), l.getCourse().getCourseId())).toList();

            for (LessonListForCourseDetailOrViewLessonDTO lesson : lessonListDTOs) {
                List<VideoListDTO> videoDTOs = videoRepository.findAll().stream().filter(v -> v.getLesson()
                        .getLessonId() == lesson.getLessonId()).map(v -> new VideoListDTO(v.getVideoId()
                        , v.getVideoName(), v.getFileVideo(), v.getLesson().getLessonId())).toList();

                List<PdfListDTO> pdfDTOs = pdfRepository.findAll().stream().filter(p -> p.getLesson()
                        .getLessonId() == lesson.getLessonId()).map(p -> new PdfListDTO(p.getPdfId()
                        , p.getPdfName(), p.getFilePdf(), p.getLesson().getLessonId())).toList();

                lesson.setVideoDTOs(videoDTOs);
                lesson.setPdfDTOs(pdfDTOs);
            }

            // if start to manager lesson
            if (video == null && pdf == null) {
                Video vid = videoRepository.findAll().stream().filter(v -> v.getLesson().getCourse().getCourseId() == courseId)
                        .findFirst().orElse(null);
                if (vid != null) {
                    video = vid.getFileVideo();
                    name = vid.getVideoName();
                    lessonId = vid.getLesson().getLessonId();
                }
            }

            data.put("lessons", lessonListDTOs);
            data.put("pdf", pdf == null ? "" : pdf);
            data.put("video", video == null ? "" : video);
            data.put("name", name == null ? "" : name);
            data.put("lessonId", lessonId == null ? 0 : lessonId);
            data.put("courseId", courseId);
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
