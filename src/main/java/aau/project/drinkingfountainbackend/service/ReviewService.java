package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.ReviewRequestDTO;
import aau.project.drinkingfountainbackend.persistence.entity.*;
import aau.project.drinkingfountainbackend.persistence.projection.ReviewsScoreSumProjection;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
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
    private final DrinkingFountainRepository drinkingFountainRepository;
    private final LoginService loginService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, DrinkingFountainRepository drinkingFountainRepository, LoginService loginService, JwtTokenService jwtTokenService) {
        this.reviewRepository = reviewRepository;
        this.drinkingFountainRepository = drinkingFountainRepository;
        this.loginService = loginService;
        this.jwtTokenService = jwtTokenService;
    }

    @Transactional
    public void addReview(ReviewRequestDTO reviewRequestDTO, HttpServletRequest httpServletRequest) {
        Optional<UserEntity> userEntity = loginService.getUserById(jwtTokenService.getUserIdFromToken(httpServletRequest));

        if (userEntity.isEmpty()) {
            throw new NoSuchElementException("User not found in database");
        }

        Optional<DrinkingFountainEntity> drinkingFountainEntity = drinkingFountainRepository.findById(reviewRequestDTO.drinkingFountainId());


        if (drinkingFountainEntity.isEmpty()) {
            throw new NoSuchElementException("Drinking fountain not found in database");
        }

        ReviewsScoreSumProjection result = reviewRepository.getReviewSumAndCount(drinkingFountainEntity.get().getId());
        double newScore = (reviewRequestDTO.stars() + result.getSum()) / (result.getCount() + 1d);
        drinkingFountainEntity.get().setScore(newScore);

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .text(reviewRequestDTO.text())
                .stars(reviewRequestDTO.stars())
                .type(reviewRequestDTO.type())
                .userEntity(userEntity.get())
                .createdAt(ZonedDateTime.now())
                .drinkingFountain(drinkingFountainEntity.get())
                .build();

        reviewRepository.save(reviewEntity);
    }
}
