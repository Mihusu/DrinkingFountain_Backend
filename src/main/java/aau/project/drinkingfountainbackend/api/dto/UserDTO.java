package aau.project.drinkingfountainbackend.api.dto;

import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;

public record UserDTO (
        String userName,
        String password,
        UserEntity.RoleType role,
        String createAt) {
}
