package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.FountainImageDTO;
import aau.project.drinkingfountainbackend.api.dto.ReviewDTO;
import aau.project.drinkingfountainbackend.api.dto.ReviewImageDTO;
import aau.project.drinkingfountainbackend.api.dto.ReviewRequestDTO;
import aau.project.drinkingfountainbackend.persistence.entity.*;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import aau.project.drinkingfountainbackend.persistence.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    ReviewRepository reviewRepository;
    LoginService loginService;

    DrinkingFountainRepository drinkingFountainRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, LoginService loginService) {
        this.reviewRepository = reviewRepository;
        this.loginService = loginService;
    }

    public void deleteReview(int id) {

    }

    public void editReview(ReviewDTO reviewDTO) {

    }

    @Transactional
    public void addReview(ReviewRequestDTO reviewRequestDTO) {

        List<String> base64Images = reviewRequestDTO.base64Images();
        List<ReviewImageEntity> reviewImageEntities = new ArrayList<>();

        // Populate reviewImageEntities with base64Images
        for (String base64Image : base64Images) {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Create a new ReviewImageEntity object
            ReviewImageEntity reviewImageEntity = new ReviewImageEntity();
            reviewImageEntity.setImage(imageBytes);
            reviewImageEntity.setCreatedAt(ZonedDateTime.now());

            // Add the reviewImageEntity to the list
            reviewImageEntities.add(reviewImageEntity);
        }

        Optional<UserEntity> userEntity = loginService.getUserById(1);

        if (userEntity.isEmpty()) {
            throw new NoSuchElementException("Item not found in database");
        }

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .text(reviewRequestDTO.text())
                .stars(reviewRequestDTO.stars())
                .reviewImageEntities(reviewImageEntities)
                .userEntity(userEntity.get())
                .createdAt(ZonedDateTime.now())
                .build();

        reviewRepository.save(reviewEntity);
    }
}
