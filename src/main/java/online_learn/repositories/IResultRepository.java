package online_learn.repositories;

import online_learn.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResultRepository extends JpaRepository<Result, Integer>, CrudRepository<Result, Integer> {
}
