package online_learn.services.manager_course;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.PageSizeConst;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.category_dto.CategoryListDTO;
import online_learn.dtos.course_dto.GetStudentOrTeacherCoursesDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.entity.Course;
import online_learn.paging.Pagination;
import online_learn.repositories.ICategoryRepository;
import online_learn.repositories.ICourseRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class ManagerCourseService extends BaseService implements IManagerCourseService {

    private final ICourseRepository courseRepository;
    private final ICategoryRepository categoryRepository;

    public ManagerCourseService(ICourseRepository courseRepository, ICategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
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
            List<CategoryListDTO> categories = categoryRepository.findAll().stream().map(c -> new CategoryListDTO(c.getCategoryId(), c.getCategoryName()))
                    .toList();

            data.put("categories", categories);
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
