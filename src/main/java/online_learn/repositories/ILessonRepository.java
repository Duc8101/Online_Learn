package online_learn.repositories;

import online_learn.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILessonRepository extends JpaRepository<Lesson, Integer>, CrudRepository<Lesson, Integer> {
}
