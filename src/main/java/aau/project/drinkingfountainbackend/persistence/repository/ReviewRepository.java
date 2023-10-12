package aau.project.drinkingfountainbackend.persistence.repository;


import aau.project.drinkingfountainbackend.persistence.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer>  {

}
