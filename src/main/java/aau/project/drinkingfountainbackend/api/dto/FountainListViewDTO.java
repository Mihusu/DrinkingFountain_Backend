package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;

public record FountainListViewDTO(
        int id,
        double distance,
        double latitude,
        double longitude,
        DrinkingFountainEntity.FountainType type,
        double score) {
}
