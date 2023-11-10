package aau.project.drinkingfountainbackend.service.model;

import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;

public record UserRoleInformation(int userId, UserEntity.RoleType roleType) {
}
