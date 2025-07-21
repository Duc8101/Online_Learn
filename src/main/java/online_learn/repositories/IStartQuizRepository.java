package online_learn.repositories;

import online_learn.entity.StartQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStartQuizRepository extends CrudRepository<StartQuiz, Integer>, JpaRepository<StartQuiz, Integer> {
}
