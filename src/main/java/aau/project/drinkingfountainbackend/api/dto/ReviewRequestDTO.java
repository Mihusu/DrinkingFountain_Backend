package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;

import java.time.ZonedDateTime;
import java.util.List;

public record ReviewRequestDTO(
        String text,
        int stars,
        List<String> base64Images,
        DrinkingFountainEntity.FountainType type,
        int drinkingFountainId,
        ZonedDateTime createAt) {

}
