package aau.project.drinkingfountainbackend.api.dto;

import java.util.List;

public record ReviewRequestDTO(
        String text,
        int stars,
        List<String> base64Images,
        String fountainType,
        int drinkingFountainId,
        String createAt) {

}
