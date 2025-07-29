package online_learn.services.manager_course;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.PageSizeConst;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.category_dto.CategoryListDTO;
import online_learn.dtos.course_dto.CourseCreateUpdateDTO;
import online_learn.dtos.course_dto.GetStudentOrTeacherCoursesDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.entity.Category;
import online_learn.entity.Course;
import online_learn.entity.User;
import online_learn.paging.Pagination;
import online_learn.repositories.ICategoryRepository;
import online_learn.repositories.ICourseRepository;
import online_learn.repositories.IUserRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class ManagerCourseService extends BaseService implements IManagerCourseService {

    private final ICourseRepository courseRepository;
    private final ICategoryRepository categoryRepository;
    private final IUserRepository userRepository;

    public ManagerCourseService(ICourseRepository courseRepository, ICategoryRepository categoryRepository
            , IUserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    private Stream<Course> getStream(int teacherId) {
        return courseRepository.findAll().stream().filter(c -> !c.isDeleted() && c.getCreator().getUserId() == teacherId);
    }

    @Override
    public ResponseBase list(int page, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, true, true, true);

            UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
            Supplier<Stream<Course>> supplier = () -> getStream(user.getUserId());

            List<GetStudentOrTeacherCoursesDTO> courses = supplier.get().skip((long) PageSizeConst.MANAGER_COURSE_LIST_PAGE * (page - 1))
                    .limit(PageSizeConst.MANAGER_COURSE_LIST_PAGE).map(c -> new GetStudentOrTeacherCoursesDTO(c.getCourseId()
                            , c.getCourseName(), c.getImage(), c.getCategory().getCategoryId(), c.getCreator().getUserId()
                            , c.getCreator().getFullName(), c.getDescription())).toList();

            Pagination<GetStudentOrTeacherCoursesDTO> pagination = new Pagination<>();
            pagination.setCurrentPage(page);
            pagination.setList(courses);
            pagination.setPageSize(PageSizeConst.MANAGER_COURSE_LIST_PAGE);
            pagination.setTotalElement(supplier.get().count());
            pagination.setFirstUrl("/ManagerCourse");
            pagination.setNextUrl(String.format("/ManagerCourse?page=%d", page + 1));
            pagination.setPreUrl(String.format("/ManagerCourse?page=%d", page - 1));
            pagination.setLastUrl(String.format("/ManagerCourse?page=%d", pagination.getNumberPage()));

            data.put("pagination", pagination);
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
    public ResponseBase create() {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, true, true, true);
            setDataCategories(data);
            return new ResponseBase(StatusCodeConst.OK, data);
        } catch (Exception e) {
            data.clear();
            data.put("error", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }

    private void setDataCategories(Map<String, Object> data) {
        List<CategoryListDTO> categories = categoryRepository.findAll().stream().map(c -> new CategoryListDTO(c.getCategoryId(), c.getCategoryName()))
                .toList();

        data.put("categories", categories);
    }

    @Override
    public ResponseBase create(CourseCreateUpdateDTO DTO, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            UserProfileInfoDTO userProfileInfoDTO = (UserProfileInfoDTO) session.getAttribute("user");
            User creator = userRepository.findById(userProfileInfoDTO.getUserId()).orElseThrow(() -> new RuntimeException("Not found user"));
            Category category = categoryRepository.findById(DTO.getCategoryId()).orElseThrow(() -> new RuntimeException("Not found category"));

            setValueForHeaderFooter(data, true, true, true, true);
            setDataCategories(data);

            if (courseRepository.findAll().stream().anyMatch(c -> c.getCourseName().equalsIgnoreCase(DTO.getCourseName().trim())
                    && c.getCategory().getCategoryId() == DTO.getCategoryId() && !c.isDeleted())) {
                data.put("error", "Course already exists");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            Course course = new Course();
            course.setCategory(category);
            course.setCourseName(DTO.getCourseName().trim());
            course.setDescription(DTO.getDescription() == null || DTO.getDescription().trim().isEmpty() ? null : DTO.getDescription().trim());
            course.setCreator(creator);
            course.setImage(DTO.getImage().trim());
            course.setCreatedAt(LocalDateTime.now());
            course.setUpdatedAt(LocalDateTime.now());
            course.setDeleted(false);
            courseRepository.save(course);

            data.put("success", "Create successful");
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
    public ResponseBase update(int courseId, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            Course course = courseRepository.findAll().stream().filter(c -> c.getCourseId() == courseId && !c.isDeleted())
                    .findFirst().orElse(null);
            if (course == null) {
                data.put("error", "Course not found");
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            UserProfileInfoDTO userProfileInfoDTO = (UserProfileInfoDTO) session.getAttribute("user");
            if (course.getCreator().getUserId() != userProfileInfoDTO.getUserId()) {
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            setValueForHeaderFooter(data, true, true, true, true);
            setDataCategories(data);

            GetStudentOrTeacherCoursesDTO dto = new GetStudentOrTeacherCoursesDTO();
            dto.setCourseId(courseId);
            dto.setCourseName(course.getCourseName());
            dto.setDescription(course.getDescription());
            dto.setCategoryId(course.getCategory().getCategoryId());
            dto.setCreatorId(course.getCreator().getUserId());
            dto.setImage(course.getImage());
            dto.setCreatorName(course.getCreator().getFullName());

            data.put("course", dto);
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
    public ResponseBase update(int courseId, CourseCreateUpdateDTO DTO, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            Course course = courseRepository.findAll().stream().filter(c -> c.getCourseId() == courseId && !c.isDeleted())
                    .findFirst().orElse(null);
            if (course == null) {
                data.put("error", "Course not found");
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            setValueForHeaderFooter(data, true, true, true, true);
            setDataCategories(data);

            GetStudentOrTeacherCoursesDTO studentOrTeacherCoursesDTO = new GetStudentOrTeacherCoursesDTO();
            studentOrTeacherCoursesDTO.setCourseId(courseId);
            studentOrTeacherCoursesDTO.setCourseName(course.getCourseName());
            studentOrTeacherCoursesDTO.setDescription(course.getDescription());
            studentOrTeacherCoursesDTO.setCategoryId(course.getCategory().getCategoryId());
            studentOrTeacherCoursesDTO.setCreatorId(course.getCreator().getUserId());
            studentOrTeacherCoursesDTO.setImage(course.getImage());
            studentOrTeacherCoursesDTO.setCreatorName(course.getCreator().getFullName());

            data.put("course", studentOrTeacherCoursesDTO);

            if (courseRepository.findAll().stream().anyMatch(c -> c.getCourseName().equalsIgnoreCase(DTO.getCourseName().trim())
                    && c.getCategory().getCategoryId() == DTO.getCategoryId() && !c.isDeleted() && c.getCourseId() != courseId)) {
                data.put("error", "Course already exists");
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            Category category = categoryRepository.findById(DTO.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

            course.setCourseName(DTO.getCourseName());
            course.setDescription(DTO.getDescription() == null || DTO.getDescription().trim().isEmpty() ? null : DTO.getDescription().trim());
            course.setCategory(category);
            course.setImage(DTO.getImage());
            course.setUpdatedAt(LocalDateTime.now());
            courseRepository.save(course);

            studentOrTeacherCoursesDTO.setCourseName(DTO.getCourseName());
            studentOrTeacherCoursesDTO.setDescription(DTO.getDescription() == null || DTO.getDescription().trim().isEmpty() ? null : DTO.getDescription().trim());
            studentOrTeacherCoursesDTO.setCategoryId(DTO.getCategoryId());
            studentOrTeacherCoursesDTO.setImage(DTO.getImage());

            data.put("course", studentOrTeacherCoursesDTO);
            data.put("success", "Update successful");
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
    public ResponseBase delete(int courseId, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            Course course = courseRepository.findAll().stream().filter(c -> c.getCourseId() == courseId && !c.isDeleted())
                    .findFirst().orElse(null);
            if (course == null) {
                data.put("error", "Course not found");
                data.put("code", StatusCodeConst.NOT_FOUND);
                return new ResponseBase(StatusCodeConst.NOT_FOUND, data);
            }

            UserProfileInfoDTO userProfileInfoDTO = (UserProfileInfoDTO) session.getAttribute("user");
            if (course.getCreator().getUserId() != userProfileInfoDTO.getUserId()) {
                return new ResponseBase(StatusCodeConst.BAD_REQUEST, data);
            }

            course.setDeleted(true);
            courseRepository.save(course);
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
