package online_learn.services.my_course;

import jakarta.servlet.http.HttpSession;
import online_learn.constants.PageSizeConst;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.course_dto.GetStudentOrTeacherCoursesDTO;
import online_learn.dtos.user_dto.UserProfileInfoDTO;
import online_learn.entity.Course;
import online_learn.paging.Pagination;
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
public class MyCourseService extends BaseService implements IMyCourseService {

    private final ICourseRepository courseRepository;

    public MyCourseService(ICourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    private Stream<Course> getStream(int studentId) {
        return courseRepository.findAll().stream().filter(c -> !c.isDeleted() && c.getEnrollCourses()
                .stream().anyMatch(ec -> ec.getStudent().getUserId() == studentId));
    }

    @Override
    public ResponseBase index(String page, HttpSession session) {
        Map<String, Object> data = new HashMap<>();
        try {
            setValueForHeaderFooter(data, true, true, true, true);
            int currentPage = page == null ? 1 : Integer.parseInt(page);

            UserProfileInfoDTO user = (UserProfileInfoDTO) session.getAttribute("user");
            Supplier<Stream<Course>> supplier = () -> getStream(user.getUserId());

            List<GetStudentOrTeacherCoursesDTO> courses = supplier.get().skip((long) PageSizeConst.MY_COURSE_PAGE * (currentPage - 1))
                    .limit(PageSizeConst.MY_COURSE_PAGE).map(c -> new GetStudentOrTeacherCoursesDTO(c.getCourseId()
                            , c.getCourseName(), c.getImage(), c.getCategory().getCategoryId(), c.getCreator().getUserId()
                            , c.getCreator().getFullName(), c.getDescription())).toList();

            Pagination<GetStudentOrTeacherCoursesDTO> pagination = new Pagination<>();
            pagination.setCurrentPage(currentPage);
            pagination.setList(courses);
            pagination.setPageSize(PageSizeConst.MY_COURSE_PAGE);
            pagination.setTotalElement(supplier.get().count());
            pagination.setFirstUrl("/MyCourse");
            pagination.setNextUrl(String.format("/MyCourse?page=%d", currentPage + 1));
            pagination.setPreUrl(String.format("/MyCourse?page=%d", currentPage - 1));
            pagination.setLastUrl(String.format("/MyCourse?page=%d", pagination.getNumberPage()));

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
}
