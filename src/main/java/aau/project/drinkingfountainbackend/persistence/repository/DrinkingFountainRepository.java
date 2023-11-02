package aau.project.drinkingfountainbackend.persistence.repository;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.projection.DrinkingFountainMapProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrinkingFountainRepository extends JpaRepository<DrinkingFountainEntity, Integer> {
    Optional<DrinkingFountainEntity> getFirstByIdAndApproved(int id, boolean approved);

    @Query("SELECT df FROM DrinkingFountainEntity df WHERE df.approved = :approved")
    List<DrinkingFountainMapProjection> findAllByApprovedMapped(@Param("approved") boolean approved);

    @Query("SELECT df FROM DrinkingFountainEntity df WHERE df.approved = :approved")
    List<DrinkingFountainEntity> findAllByApprovedEntity(@Param("approved") boolean approved);

    @Modifying
    @Transactional
    @Query("UPDATE DrinkingFountainEntity e SET e.approved = true WHERE e.id = :id")
    void approveById(@Param("id") int id);

    void deleteById(int id);
}
