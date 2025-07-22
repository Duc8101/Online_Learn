package online_learn.services.courses;

import online_learn.constants.PageSizeConst;
import online_learn.constants.StatusCodeConst;
import online_learn.dtos.course_dto.CourseDetailDTO;
import online_learn.dtos.lesson_dto.LessonListForCourseDetailOrViewLessonDTO;
import online_learn.entity.Course;
import online_learn.paging.Pagination;
import online_learn.repositories.ICategoryRepository;
import online_learn.repositories.ICourseRepository;
import online_learn.repositories.IEnrollCourseRepository;
import online_learn.responses.ResponseBase;
import online_learn.services.base.BaseService;
import online_learn.dtos.category_dto.CategoryListDTO;
import online_learn.dtos.course_dto.GetAllCoursesDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;
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

    private Stream<Course> getStream(Integer categoryId) {
        Stream<Course> stream = courseRepository.findAll().stream().filter(c -> !c.isDeleted());
        if (categoryId != null) {
            stream = stream.filter(c -> c.getCategory().getCategoryId() == categoryId);
        }
        return stream;
    }

    @Override
    public ResponseBase list(Integer categoryId, Boolean orderBy, Integer page, Integer studentId) {
        Map<String, Object> data = new HashMap<>();
        try {
            int currentPage = page == null ? 1 : page;
            // get categories
            List<CategoryListDTO> categories = categoryRepository.findAll().stream().map(c -> new CategoryListDTO(c.getCategoryId(), c.getCategoryName()))
                    .toList();

            // ------------------------- get courses ----------------------------
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

            StringBuilder preUrl = new StringBuilder("/Courses");
            StringBuilder nextUrl = new StringBuilder("/Courses");
            StringBuilder firstUrl = new StringBuilder("/Courses");
            StringBuilder lastUrl = new StringBuilder("/Courses");

            if (orderBy == null) {
                // if not choose category
                if (categoryId == null) {
                    preUrl.append(String.format("?page=%d", currentPage - 1));
                    nextUrl.append(String.format("?page=%d", currentPage + 1));
                    lastUrl.append(String.format("?page=%d", pagination.getNumberPage()));
                } else {
                    preUrl.append(String.format("?categoryId=%d&page=%d", categoryId, currentPage - 1));
                    nextUrl.append(String.format("?categoryId=%d&page=%d", categoryId, currentPage + 1));
                    firstUrl.append(String.format("?categoryId=%d", categoryId));
                    lastUrl.append(String.format("?categoryId=%d&page=%d", categoryId, pagination.getNumberPage()));
                }
            } else {
                // if not choose category
                if (categoryId == null) {
                    preUrl.append(String.format("?orderBy=%b&page=%d", orderBy, currentPage - 1));
                    nextUrl.append(String.format("?orderBy=%b&page=%d", orderBy, currentPage + 1));
                    firstUrl.append(String.format("?orderBy=%b", orderBy));
                    lastUrl.append(String.format("?orderBy=%b&page=%d", orderBy, pagination.getNumberPage()));
                } else {
                    preUrl.append(String.format("?categoryId=%d&orderBy=%b&page=%d", categoryId, orderBy, currentPage - 1));
                    nextUrl.append(String.format("?categoryId=%d&orderBy=%b&page=%d", categoryId, orderBy, currentPage + 1));
                    firstUrl.append(String.format("?categoryId=%d&orderBy=%b", categoryId, orderBy));
                    lastUrl.append(String.format("?categoryId=%d&orderBy=%b&page=%d", categoryId, orderBy, pagination.getNumberPage()));
                }
            }

            pagination.setPreUrl(preUrl.toString());
            pagination.setNextUrl(nextUrl.toString());
            pagination.setFirstUrl(firstUrl.toString());
            pagination.setLastUrl(lastUrl.toString());

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
}
