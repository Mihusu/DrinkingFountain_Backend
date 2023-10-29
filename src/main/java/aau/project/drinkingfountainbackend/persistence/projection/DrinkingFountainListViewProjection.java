package aau.project.drinkingfountainbackend.persistence.projection;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;

public interface DrinkingFountainListViewProjection {
    int getId();
    double getDistance();
    DrinkingFountainEntity.FountainType getType();
    double getScore();
}
