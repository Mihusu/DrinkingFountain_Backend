package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import java.time.ZonedDateTime;

public record ReviewDTO (
        String text,
        int stars,
        DrinkingFountainEntity.FountainType type,
        String username,
        ZonedDateTime createdAt) {
}
