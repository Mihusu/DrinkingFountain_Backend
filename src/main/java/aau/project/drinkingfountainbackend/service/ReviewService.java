package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.FountainImageDTO;
import aau.project.drinkingfountainbackend.api.dto.ReviewDTO;
import aau.project.drinkingfountainbackend.persistence.entity.FountainImageEntity;
import aau.project.drinkingfountainbackend.persistence.entity.ReviewEntity;
import aau.project.drinkingfountainbackend.persistence.entity.ReviewImageEntity;
import aau.project.drinkingfountainbackend.persistence.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewDTO> getReviews(int id) {
        List<ReviewEntity> reviewEntity = reviewRepository.getAllById(id);

        return reviewEntity.stream()
            .map(entity -> {
                List<ReviewImageEntity> images = entity.getReviewImageEntities();
                // Turn each entity into a DTO
                // A function that takes a stream of image entities, make a DTO then takes a bytearray, encodes from byte[] to base64 string
                List<FountainImageDTO> fountainImageDTOS = images.stream()
                        .map(image -> new FountainImageDTO(
                                // From byte[] to base64 String
                                Base64.getEncoder().encodeToString(image.getImage())))
                        .collect(Collectors.toList());

            return new ReviewDTO(
                    entity.getText(),
                    entity.getStars(),
                    fountainImageDTOS,
                    entity.getType(),
                    entity.getUserEntity(),
                    entity.getDrinkingFountainEntity(),
                    entity.getCreatedAt());
            })
                .collect(Collectors.toList()); // Collect the Stream into a List
    }

    public void addReview(ReviewDTO reviewDTO) {

    }

    public void deleteReview(int id) {

    }

    public void editReview(ReviewDTO reviewDTO) {

    }
}
