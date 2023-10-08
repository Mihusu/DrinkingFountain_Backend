package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.ZonedDateTime;
import java.util.List;

public record ReviewDTO (
        String text,
        int stars,
        List<FountainImageDTO> fountainImages,
        DrinkingFountainEntity.FountainType type,
        @ManyToOne
        @JoinColumn(name="user_id", nullable=false)
        UserEntity userEntity,
        @ManyToOne
        @JoinColumn(name="drinking_fountain_id", nullable=false)
        DrinkingFountainEntity drinkingFountainEntity,
        ZonedDateTime createdAt) {
}
