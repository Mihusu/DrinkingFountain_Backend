package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.ReviewDTO;
import aau.project.drinkingfountainbackend.api.dto.ReviewRequestDTO;
import aau.project.drinkingfountainbackend.persistence.entity.*;
import aau.project.drinkingfountainbackend.persistence.repository.ReviewImageRepository;
import aau.project.drinkingfountainbackend.persistence.repository.ReviewRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final LoginService loginService;
    private final DrinkingFountainService drinkingFountainService;
    private final JwtTokenService jwtTokenService;


    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReviewImageRepository reviewImageRepository, LoginService loginService, DrinkingFountainService drinkingFountainService, JwtTokenService jwtTokenService) {
        this.reviewRepository = reviewRepository;
        this.reviewImageRepository = reviewImageRepository;
        this.loginService = loginService;
        this.drinkingFountainService = drinkingFountainService;
        this.jwtTokenService = jwtTokenService;
    }

    public void deleteReview(int id) {
        //@TODO
    }

    public void editReview(ReviewDTO reviewDTO) {
        //@TODO
    }

    @Transactional
    public void addReview(ReviewRequestDTO reviewRequestDTO, HttpServletRequest httpServletRequest) {
        Optional<UserEntity> userEntity = loginService.getUserById(jwtTokenService.getUserIdFromToken(httpServletRequest));

        if (userEntity.isEmpty()) {
            throw new NoSuchElementException("Item not found in database");
        }

        Optional<DrinkingFountainEntity> drinkingFountainEntity = drinkingFountainService.getDrinkingFountainEntity(reviewRequestDTO.drinkingFountainId());

        if (drinkingFountainEntity.isEmpty()) {
            throw new NoSuchElementException("Item not found in database");
        }

            ReviewEntity reviewEntity = ReviewEntity.builder()
                    .text(reviewRequestDTO.text())
                    .stars(reviewRequestDTO.stars())
                    .type(DrinkingFountainEntity.FountainType.valueOf(reviewRequestDTO.fountainType()))
                    .userEntity(userEntity.get())
                    .createdAt(ZonedDateTime.now())
                    .drinkingFountain(drinkingFountainEntity.get())
                    .build();

        ReviewEntity savedReview = reviewRepository.save(reviewEntity);

        List<String> base64Images = reviewRequestDTO.base64Images();
        List<ReviewImageEntity> reviewImageEntities = new ArrayList<>();

        // Populate reviewImageEntities with base64Images
        for (String base64Image : base64Images) {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Create a new ReviewImageEntity object
            ReviewImageEntity reviewImageEntity = new ReviewImageEntity();
            reviewImageEntity.setImage(imageBytes);
            reviewImageEntity.setCreatedAt(ZonedDateTime.now());
            reviewImageEntity.setReview(savedReview);

            // Add the reviewImageEntity to the list
            reviewImageEntities.add(reviewImageEntity);
        }
        reviewImageRepository.saveAll(reviewImageEntities);
    }
}
