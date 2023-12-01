package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.EnableTestcontainers;
import aau.project.drinkingfountainbackend.api.dto.ReviewRequestDTO;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.entity.ReviewEntity;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import aau.project.drinkingfountainbackend.persistence.repository.ReviewRepository;
import aau.project.drinkingfountainbackend.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@EnableTestcontainers
public class ReviewServiceIT {

    @Autowired
    DrinkingFountainService drinkingFountainService;

    @Autowired
    DrinkingFountainRepository drinkingFountainRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenService jwtTokenService;

    @Test
    @Transactional
    void addReviewIT() {
        UserEntity userEntity = UserEntity.builder()
                .role(UserEntity.RoleType.ADMIN)
                .password("jerf1234")
                .name("Jerfer")
                .build();

        DrinkingFountainEntity drinkingFountainEntity = DrinkingFountainEntity.builder()
                .latitude(0.001)
                .longitude(0.001)
                .type(DrinkingFountainEntity.FountainType.DRINKING)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .score(5.0)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity insertedDrinkingFountain = drinkingFountainRepository.save(drinkingFountainEntity);

        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(
            "review request text",
                5,
                DrinkingFountainEntity.FountainType.DRINKING,
                insertedDrinkingFountain.getId()
        );

        UserEntity user = UserEntity.builder()
                .name("Jerf")
                .password("jerf123456")
                .role(UserEntity.RoleType.ADMIN)
                .createdAt(ZonedDateTime.now())
                .build();

        userRepository.save(user);

        String token = jwtTokenService.generateToken(user.getId(), "ADMIN");

        // Create a mocked HttpServletRequest
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("Authorization", "Bearer " + token);

        reviewService.addReview(reviewRequestDTO, mockHttpServletRequest);

        //
        //Optional<ReviewEntity> inserted = reviewRepository.findById();

        Optional<DrinkingFountainEntity> fountainEntity = drinkingFountainRepository.getFirstByIdAndApproved(insertedDrinkingFountain.getId(), true);

        //Assertions.assertTrue(fountainEntity.isPresent());

        Assertions.assertTrue(fountainEntity.isPresent());
        List<ReviewEntity> reviewEntities = fountainEntity.get().getReviewEntities();
        Assertions.assertEquals(reviewRequestDTO.text(), reviewEntities.get(0).getText());
    }
}
