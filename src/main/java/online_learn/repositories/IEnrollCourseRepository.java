package online_learn.repositories;

import online_learn.composite.key.EnrollCourseId;
import online_learn.entity.EnrollCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEnrollCourseRepository extends JpaRepository<EnrollCourse, EnrollCourseId>, CrudRepository<EnrollCourse, EnrollCourseId> {
}
