package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;

public record DrinkingFountainRequestDTO(
        double latitude,
        double longitude,
        DrinkingFountainEntity.FountainType type,
        String review,
        int score,
        String base64Images) {
}
