package online_learn.repositories;

import online_learn.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVideoRepository extends CrudRepository<Video, Integer>, JpaRepository<Video, Integer> {
}
