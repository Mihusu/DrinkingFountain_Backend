package aau.project.drinkingfountainbackend.persistence.repository;

import aau.project.drinkingfountainbackend.persistence.entity.ReviewEntity;
import aau.project.drinkingfountainbackend.persistence.projection.ReviewsScoreSumProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer>  {

    @Query("SELECT SUM(r.stars) AS sum, COUNT(r) AS count " +
            "FROM ReviewEntity r " +
            "WHERE r.drinkingFountain.id = :fountainId")
    ReviewsScoreSumProjection getReviewSumAndCount(@Param("fountainId") int fountainId);
}
