package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;

public record FountainListViewDTO(
        int id,
        double distance,
        DrinkingFountainEntity.FountainType type,
        double score) {
}
