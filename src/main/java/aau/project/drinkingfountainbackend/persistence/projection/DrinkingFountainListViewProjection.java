package aau.project.drinkingfountainbackend.persistence.projection;

import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;

public interface DrinkingFountainListViewProjection {
    int getId();
    double getDistance();
    double getLatitude();
    double getLongitude();
    DrinkingFountainEntity.FountainType getType();
    double getScore();
}
