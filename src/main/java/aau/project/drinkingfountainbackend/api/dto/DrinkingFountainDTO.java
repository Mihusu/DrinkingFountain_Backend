package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import java.time.ZonedDateTime;
import java.util.List;

public record DrinkingFountainDTO(
        int id,
        double latitude,
        double longitude,
        DrinkingFountainEntity.FountainType type,
        ZonedDateTime createdAt,
        double score,
        List<FountainImageDTO> fountainImages,
        List<ReviewDTO> reviews) {
}
