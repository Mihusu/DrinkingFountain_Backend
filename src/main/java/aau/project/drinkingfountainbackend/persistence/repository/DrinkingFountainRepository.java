package aau.project.drinkingfountainbackend.persistence.repository;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.projection.DrinkingFountainListViewProjection;
import aau.project.drinkingfountainbackend.persistence.projection.DrinkingFountainMapProjection;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT df.id as id," +
            "df.longitude as longitude," +
            "df.latitude as latitude," +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(df.latitude)) * cos(radians(df.longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(df.latitude)))) AS distance " +
            " FROM DrinkingFountainEntity df WHERE df.approved = :approved")
    List<DrinkingFountainMapProjection> findAllByApprovedMapped(@Param("lat") double lat,
                                                                @Param("lon") double lon,
                                                                @Param("approved") boolean approved);

    @Query("SELECT df FROM DrinkingFountainEntity df WHERE df.approved = :approved")
    List<DrinkingFountainEntity> findAllByApprovedEntity(@Param("approved") boolean approved);

    @Modifying
    @Transactional
    @Query("UPDATE DrinkingFountainEntity e SET e.approved = true WHERE e.id = :id")
    void approveById(@Param("id") int id);

    //Distance is in km as 6371 is the radius of the Earth in km
    @Query("SELECT df.id AS id, " +
            "df.longitude as longitude," +
            "df.latitude as latitude," +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(df.latitude)) * cos(radians(df.longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(df.latitude)))) AS distance, " +
            "df.type AS type, " +
            "df.score AS score " +
            "FROM DrinkingFountainEntity df WHERE df.approved = :approved ORDER BY distance")
    List<DrinkingFountainListViewProjection> findNearestFountains(@Param("lat") double lat,
                                                                  @Param("lon") double lon,
                                                                  @Param("approved") boolean approved,
                                                                  Pageable pageable);
}
