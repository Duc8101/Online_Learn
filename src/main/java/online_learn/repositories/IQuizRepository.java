package online_learn.repositories;

import online_learn.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuizRepository extends CrudRepository<Quiz, Integer>, JpaRepository<Quiz, Integer> {
}
