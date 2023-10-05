package aau.project.drinkingfountainbackend.persistence.repository;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrinkingFountainRepository extends JpaRepository<DrinkingFountainEntity, Integer> {
    Optional<DrinkingFountainEntity> getFirstById(int id);
}
