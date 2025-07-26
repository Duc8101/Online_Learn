package online_learn.services.courses;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.PageSizeConst;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.course_dto.CourseDetailDTO;
import online_learn.dtos.lesson_dto.LessonListForCourseDetailOrViewLessonDTO;
import online_learn.dtos.lesson_dto.LessonListForLearnCourseDTO;
import online_learn.dtos.pdf_dto.PdfListDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.dtos.video_dto.VideoListDTO;
import online_learn.entity.Course;
import online_learn.entity.EnrollCourse;
import online_learn.entity.User;
import online_learn.entity.Video;
import online_learn.paging.Pagination;
import online_learn.repositories.*;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import online_learn.dtos.category_dto.CategoryListDTO;
import online_learn.dtos.course_dto.GetAllCoursesDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class CoursesService extends BaseService implements ICoursesService {

    private final ICategoryRepository categoryRepository;
    private final ICourseRepository courseRepository;
    private final IEnrollCourseRepository enrollCourseRepository;
    private final IUserRepository userRepository;
    private final IVideoRepository videoRepository;
    private final IPdfRepository pdfRepository;

    public CoursesService(ICategoryRepository categoryRepository, ICourseRepository courseRepository, IEnrollCourseRepository enrollCourseRepository
            , IUserRepository userRepository, IVideoRepository videoRepository, IPdfRepository pdfRepository) {
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
        this.enrollCourseRepository = enrollCourseRepository;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.pdfRepository = pdfRepository;
    }

    private Stream<Course> getStream(String categoryId) {
        Stream<Course> stream = courseRepository.findAll().stream().filter(c -> !c.isDeleted());
        if (categoryId != null) {
            stream = stream.filter(c -> c.getCategory().getCategoryId() == Integer.parseInt(categoryId));
        }
        return stream;
    }

    @Override
    public ResponseBase list(String categoryId, String orderBy, String page, Integer studentId) {
        Map<String, Object> data = new HashMap<>();
        try {
            int currentPage = page == null ? 1 : Integer.parseInt(page);
            // get categories
            List<CategoryListDTO> categories = categoryRepository.findAll().stream().map(c -> new CategoryListDTO(c.getCategoryId(), c.getCategoryName()))
                    .toList();

            // ------------------------- get courses ----------------------------
            Comparator<Course> comparator;
            // if sort by updated_at desc
            if (orderBy == null) {
                comparator = Comparator.comparing(Course::getUpdatedAt).reversed();
                // if sort by course name asc
            } else if (Boolean.parseBoolean(orderBy)) {
                comparator = Comparator.comparing(Course::getCourseName);
            } else {
                comparator = Comparator.comparing(Course::getCourseName).reversed();
            }

            Supplier<Stream<Course>> streamSupplier = () -> getStream(categoryId);

            List<GetAllCoursesDTO> courses = streamSupplier.get().sorted(comparator).skip((long) PageSizeConst.COURSES_PAGE * (currentPage - 1))
                    .limit(PageSizeConst.COURSES_PAGE).map(c -> new GetAllCoursesDTO(c.getCourseId()
                            , c.getCourseName(), c.getImage(), c.getCategory().getCategoryId(), c.getCreator().getUserId()
                            , c.getCreator().getFullName(), c.getDescription(), !c.getLessons().isEmpty(), false))
                    .toList();

            // if login as a student
            if (studentId != null) {
                for (GetAllCoursesDTO course : courses) {
                    course.setEnrollCourseExist(enrollCourseRepository.findAll().stream()
                            .anyMatch(ec -> ec.getCourse().getCourseId() == course.getCourseId()
                                    && ec.getStudent().getUserId() == studentId));
                }
            }

            // ------------------------- set pagination ----------------------------
            Pagination<GetAllCoursesDTO> pagination = new Pagination<>();
            pagination.setCurrentPage(currentPage);
            pagination.setList(courses);
            pagination.setPageSize(PageSizeConst.COURSES_PAGE);
            pagination.setTotalElement(streamSupplier.get().count());

            String preUrl = "/Courses";
            String nextUrl = "/Courses";
            String firstUrl = "/Courses";
            String lastUrl = "/Courses";

            if (orderBy == null) {
                // if not choose category
                if (categoryId == null) {
                    preUrl = preUrl + String.format("?page=%d", currentPage - 1);
                    nextUrl = nextUrl + String.format("?page=%d", currentPage + 1);
                    lastUrl = lastUrl + String.format("?page=%d", pagination.getNumberPage());
                } else {
                    preUrl = preUrl + String.format("?categoryId=%d&page=%d", categoryId, currentPage - 1);
                    nextUrl = nextUrl + String.format("?categoryId=%d&page=%d", categoryId, currentPage + 1);
                    firstUrl = firstUrl + String.format("?categoryId=%d", categoryId);
                    lastUrl = lastUrl + String.format("?categoryId=%d&page=%d", categoryId, pagination.getNumberPage());
                }
            } else {
                // if not choose category
                if (categoryId == null) {
                    preUrl = preUrl + String.format("?orderBy=%b&page=%d", orderBy, currentPage - 1);
                    nextUrl = nextUrl + String.format("?orderBy=%b&page=%d", orderBy, currentPage + 1);
                    firstUrl = firstUrl + String.format("?orderBy=%b", orderBy);
                    lastUrl = lastUrl + String.format("?orderBy=%b&page=%d", orderBy, pagination.getNumberPage());
                } else {
                    preUrl = preUrl + String.format("?categoryId=%d&orderBy=%b&page=%d", categoryId, orderBy, currentPage - 1);
                    nextUrl = nextUrl + String.format("?categoryId=%d&orderBy=%b&page=%d", categoryId, orderBy, currentPage + 1);
                    firstUrl = firstUrl + String.format("?categoryId=%d&orderBy=%b", categoryId, orderBy);
                    lastUrl = lastUrl + String.format("?categoryId=%d&orderBy=%b&page=%d", categoryId, orderBy, pagination.getNumberPage());
                }
            }

            pagination.setPreUrl(preUrl);
            pagination.setNextUrl(nextUrl);
            pagination.setFirstUrl(firstUrl);
            pagination.setLastUrl(lastUrl);

            // ------------------------- set data ----------------------------
            data.put("pagination", pagination);
            data.put("categories", categories);
            data.put("orderBy", orderBy);
            data.put("categoryId", categoryId);
            setValueForHeaderFooter(data, true, true, true, true);
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
    public ResponseBase detail(int courseId, Integer studentId) {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, true, true, true);
            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                data.put("error", String.format("Course with id = %d does not exist", courseId));
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            CourseDetailDTO courseDetailDTO = new CourseDetailDTO();
            courseDetailDTO.setCourseId(courseId);
            courseDetailDTO.setCourseName(course.getCourseName());
            courseDetailDTO.setImage(course.getImage());
            courseDetailDTO.setDescription(course.getDescription());
            courseDetailDTO.setCategoryId(course.getCategory().getCategoryId());
            courseDetailDTO.setCreatorId(course.getCreator().getUserId());
            courseDetailDTO.setCreatorName(course.getCreator().getFullName());
            courseDetailDTO.setLessonExist(!course.getLessons().isEmpty());

            List<LessonListForCourseDetailOrViewLessonDTO> lessonListDTOs = course.getLessons().stream()
                    .map(l -> new LessonListForCourseDetailOrViewLessonDTO(l.getLessonId(), l.getLessonName()
                            , l.getCourse().getCourseId())).toList();
            courseDetailDTO.setLessonListDTOs(lessonListDTOs);

            // if login as a student
            if (studentId != null) {
                courseDetailDTO.setEnrollCourseExist(enrollCourseRepository.findAll().stream()
                        .anyMatch(ec -> ec.getCourse().getCourseId() == course.getCourseId()
                                && ec.getStudent().getUserId() == studentId));
            }

            data.put("course", courseDetailDTO);
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
    public ResponseBase enrollCourse(int courseId, int studentId) {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, true, true, true);
            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                data.put("error", String.format("Course with id = %d does not exist", courseId));
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            User student = userRepository.findById(studentId).orElse(null);
            if (student == null) {
                data.put("error", String.format("User with id = %d does not exist", studentId));
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            // if student not enroll course
            if (enrollCourseRepository.findAll().stream().noneMatch(ec -> ec.getCourse().getCourseId() == course.getCourseId() && ec.getStudent().getUserId() == studentId)) {
                EnrollCourse enrollCourse = new EnrollCourse(course, student);
                enrollCourse.setCreatedAt(LocalDateTime.now());
                enrollCourseRepository.save(enrollCourse);
            }

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
    public ResponseBase learnCourse(int courseId, String video, String name, String pdf, Integer lessonId, Integer videoId, Integer pdfId, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                setValueForHeaderFooter(data, true, true, true, true);
                data.put("error", String.format("Course with id = %d does not exist", courseId));
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");

            // if student not enroll course
            if (enrollCourseRepository.findAll().stream().noneMatch(ec -> ec.getCourse().getCourseId() == courseId && ec.getStudent().getUserId() == user.getUserId())) {
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            setValueForHeaderFooter(data, false, true, false, false);
            List<LessonListForLearnCourseDTO> lessons = course.getLessons().stream().map(l -> new LessonListForLearnCourseDTO(l.getLessonId()
                    , l.getLessonName(), l.getCourse().getCourseId(), !l.getQuizzes().isEmpty())).toList();

            for (LessonListForLearnCourseDTO lesson : lessons) {
                List<VideoListDTO> videoDTOs = videoRepository.findAll().stream().filter(v -> v.getLesson()
                        .getLessonId() == lesson.getLessonId()).map(v -> new VideoListDTO(v.getVideoId()
                        , v.getVideoName(), v.getFileVideo(), v.getLesson().getLessonId())).toList();

                List<PdfListDTO> pdfDTOs = pdfRepository.findAll().stream().filter(p -> p.getLesson()
                        .getLessonId() == lesson.getLessonId()).map(p -> new PdfListDTO(p.getPdfId()
                        , p.getPdfName(), p.getFilePdf(), p.getLesson().getLessonId())).toList();

                lesson.setVideoDTOs(videoDTOs);
                lesson.setPdfDTOs(pdfDTOs);
            }

            // if start to learn course
            if (video == null && pdf == null) {
                Video vid = videoRepository.findAll().stream().filter(v -> v.getLesson().getCourse().getCourseId() == courseId)
                        .findFirst().orElse(null);
                if (vid != null) {
                    video = vid.getFileVideo();
                    name = vid.getVideoName();
                    videoId = vid.getVideoId();
                    lessonId = vid.getLesson().getLessonId();
                }
            }

            data.put("lessons", lessons);
            data.put("pdf", pdf == null ? "" : pdf);
            data.put("video", video == null ? "" : video);
            data.put("name", name == null ? "" : name);
            data.put("lessonId", lessonId == null ? 0 : lessonId);
            data.put("courseId", courseId);
            data.put("videoId", videoId);
            data.put("pdfId", pdfId);
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
