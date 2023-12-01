package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import java.util.List;

public record ReviewRequestDTO(
        String text,
        int stars,
        DrinkingFountainEntity.FountainType type,
        int drinkingFountainId) {

}
