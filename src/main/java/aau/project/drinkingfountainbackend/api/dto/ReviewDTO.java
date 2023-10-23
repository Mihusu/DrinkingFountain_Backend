package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import java.time.ZonedDateTime;
import java.util.List;

public record ReviewDTO (
        String text,
        int stars,
        List<ReviewImageDTO> fountainImages,
        DrinkingFountainEntity.FountainType type,
        String username,
        ZonedDateTime createdAt) {
}