package online_learn.services.manager_lesson;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.LessonConst;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.lesson_dto.LessonCreateDTO;
import online_learn.dtos.lesson_dto.LessonListForCourseDetailOrViewLessonDTO;
import online_learn.dtos.lesson_dto.LessonUpdateDTO;
import online_learn.dtos.pdf_dto.PdfListDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.dtos.video_dto.VideoListDTO;
import online_learn.entity.Course;
import online_learn.entity.Lesson;
import online_learn.entity.Video;
import online_learn.repositories.ICourseRepository;
import online_learn.repositories.ILessonRepository;
import online_learn.repositories.IPdfRepository;
import online_learn.repositories.IVideoRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerLessonService extends BaseService implements IManagerLessonService {

    private final ICourseRepository courseRepository;
    private final IVideoRepository videoRepository;
    private final IPdfRepository pdfRepository;
    private final ILessonRepository lessonRepository;

    public ManagerLessonService(ICourseRepository courseRepository, IVideoRepository videoRepository, IPdfRepository pdfRepository
            , ILessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.videoRepository = videoRepository;
        this.pdfRepository = pdfRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public ResponseBase list(int courseId, String video, String name, String pdf, Integer lessonId, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
            Course course = courseRepository.findAll().stream().filter(c -> c.getCourseId() == courseId
                    && !c.isDeleted() && c.getCreator().getUserId() == user.getUserId()).findFirst().orElse(null);

            if (course == null) {
                data.put("error", String.format("Course with id = %d does not exist", courseId));
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            setData(data, course, video, name, pdf, lessonId);
            return new ResponseBase(StatusCodeConst.OK, data);
        } catch (Exception e) {
            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }

    private void setData(Map<String, Object> data, Course course, String video, String name, String pdf, Integer lessonId) {
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
            Video vid = videoRepository.findAll().stream().filter(v -> v.getLesson().getCourse().getCourseId() == course.getCourseId())
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
        data.put("courseId", course.getCourseId());
    }

    @Override
    public ResponseBase create(LessonCreateDTO DTO) {
        Map<String, Object> data = new HashMap<>();
        try {
            Course course = courseRepository.findById(DTO.getCourseId()).orElse(null);
            if (course == null || course.isDeleted()) {
                data.put("error", "Course not found or might be deleted");
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            setData(data, course, null, null, null, null);

            setValueForHeaderFooter(data, false, true, false, false);
            if (DTO.getLessonName().trim().isEmpty()) {
                data.put("error", "Lesson name not empty");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            if (DTO.getLessonName().trim().length() > LessonConst.MAX_LESSON_NAME) {
                data.put("error", String.format("Lesson name max %d characters", LessonConst.MAX_LESSON_NAME));
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            if (lessonRepository.findAll().stream().anyMatch(l -> l.getLessonName().equalsIgnoreCase(DTO.getLessonName().trim())
                    && l.getCourse().getCourseId() == DTO.getCourseId())) {
                data.put("error", "Lesson already exists");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            Lesson lesson = new Lesson();
            lesson.setLessonName(DTO.getLessonName().trim());
            lesson.setCourse(course);
            lesson.setCreatedAt(LocalDateTime.now());
            lesson.setUpdatedAt(LocalDateTime.now());
            lessonRepository.save(lesson);
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
    public ResponseBase update(int lessonId, LessonUpdateDTO DTO) {
        Map<String, Object> data = new HashMap<>();
        try {
            Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
            if (lesson == null || lesson.getCourse().isDeleted()) {
                data.put("error", "Lesson not found or course might be deleted");
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            setData(data, lesson.getCourse(), null, null, null, null);

            setValueForHeaderFooter(data, false, true, false, false);
            if (DTO.getLessonName().trim().isEmpty()) {
                data.put("error", "Lesson name not empty");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            if (DTO.getLessonName().trim().length() > LessonConst.MAX_LESSON_NAME) {
                data.put("error", String.format("Lesson name max %d characters", LessonConst.MAX_LESSON_NAME));
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            if (lessonRepository.findAll().stream().anyMatch(l -> l.getLessonName().equalsIgnoreCase(DTO.getLessonName().trim())
                    && l.getCourse().getCourseId() == lesson.getCourse().getCourseId() && l.getLessonId() != lesson.getLessonId())) {
                data.put("error", "Lesson already exists");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            lesson.setLessonName(DTO.getLessonName().trim());
            lesson.setUpdatedAt(LocalDateTime.now());
            lessonRepository.save(lesson);
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
