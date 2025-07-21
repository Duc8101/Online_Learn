package online_learn.repositories;

import online_learn.entity.Pdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPdfRepository extends CrudRepository<Pdf, Integer>, JpaRepository<Pdf, Integer> {
}
