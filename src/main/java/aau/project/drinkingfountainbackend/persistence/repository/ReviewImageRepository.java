package aau.project.drinkingfountainbackend.persistence.repository;

import aau.project.drinkingfountainbackend.persistence.entity.ReviewImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImageEntity, Integer> {
}
