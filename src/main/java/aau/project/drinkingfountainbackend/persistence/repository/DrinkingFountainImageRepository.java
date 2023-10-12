package aau.project.drinkingfountainbackend.persistence.repository;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinkingFountainImageRepository extends JpaRepository<DrinkingFountainImageEntity, Integer> {
}
