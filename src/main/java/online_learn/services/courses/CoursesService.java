package online_learn.services.courses;

import online_learn.constants.PageSizeConst;
import online_learn.constants.StatusCodeConst;
import online_learn.entity.Course;
import online_learn.repositories.ICategoryRepository;
import online_learn.repositories.ICourseRepository;
import online_learn.repositories.IEnrollCourseRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import online_learn.view_models.categories.CategoryViewModel;
import online_learn.view_models.courses.GetAllCoursesViewModel;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class CoursesService extends BaseService implements ICoursesService {

    private final ICategoryRepository categoryRepository;
    private final ICourseRepository courseRepository;
    private final IEnrollCourseRepository enrollCourseRepository;

    public CoursesService(ICategoryRepository categoryRepository, ICourseRepository courseRepository, IEnrollCourseRepository enrollCourseRepository) {
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
        this.enrollCourseRepository = enrollCourseRepository;
    }

    @Override
    public ResponseBase list(Integer categoryId, Boolean orderBy, Integer page, Integer studentId) {
        Map<String, Object> data = new HashMap<>();
        try {
            int currentPage = page == null ? 1 : page;
            List<CategoryViewModel> categories = categoryRepository.findAll().stream().map(c -> new CategoryViewModel(c.getCategoryId(), c.getCategoryName()))
                    .toList();

            Comparator<Course> comparator;
            // if sort by updated_at
            if (orderBy == null) {
                comparator = Comparator.comparing(Course::getUpdatedAt).reversed();
                // if sort by course name asc
            } else if (orderBy) {
                comparator = Comparator.comparing(Course::getCourseName);
            } else {
                comparator = Comparator.comparing(Course::getCourseName).reversed();
            }

            Stream<Course> stream = courseRepository.findAll().stream().filter(c -> !c.isDeleted());
            if (categoryId != null) {
                stream = stream.filter(c -> c.getCategory().getCategoryId() == categoryId);
            }

            List<GetAllCoursesViewModel> courses = stream.sorted(comparator).skip(PageSizeConst.COURSES_PAGE * (currentPage - 1))
                    .map(c -> new GetAllCoursesViewModel(c.getCourseId(), c.getCourseName(), c.getImage()
                    , c.getCategory().getCategoryId(), c.getCreator().getUserId(), c.getCreator().getFullName()
                    , c.getDescription(), !c.getLessons().isEmpty(), false)).toList();

            if (studentId != null) {
                for (GetAllCoursesViewModel course : courses) {
                    course.setEnrollCourseExist(enrollCourseRepository.findAll().stream()
                            .anyMatch(ec -> ec.getCourse().getCourseId() == course.getCourseId()
                                    && ec.getStudent().getUserId() == studentId));
                }
            }

            return new ResponseBase(StatusCodeConst.OK, data);
        } catch (Exception e){
            data.clear();
            data.put("exception", e.getMessage() + " " + e);
            data.put("code", StatusCodeConst.INTERNAL_SERVER_ERROR);
            setValueForHeaderFooter(data, true, true, true, true);
            return new ResponseBase(StatusCodeConst.INTERNAL_SERVER_ERROR, data);
        }
    }
}
