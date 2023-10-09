package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import java.time.ZonedDateTime;
import java.util.List;

public record ReviewDTO (
        String text,
        int stars,
        List<FountainImageDTO> fountainImages,
        DrinkingFountainEntity.FountainType type,
        String username,
        ZonedDateTime createdAt) {
}
