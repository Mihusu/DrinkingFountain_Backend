package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.ReviewRequestDTO;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.entity.ReviewEntity;
import aau.project.drinkingfountainbackend.persistence.entity.ReviewsScoreSumProjectionMock;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import aau.project.drinkingfountainbackend.persistence.projection.ReviewsScoreSumProjection;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import aau.project.drinkingfountainbackend.persistence.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.ZonedDateTime;
import java.util.*;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private DrinkingFountainRepository drinkingFountainRepository;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void addReviewWithoutUser() {
        // Attributes for review
        String text = "My first drinking fountain review";
        int stars = 4;
        DrinkingFountainEntity.FountainType type = DrinkingFountainEntity.FountainType.DRINKING;
        int drinkingFountainId = 0;

        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(text, stars, type, drinkingFountainId);
        Mockito.when(loginService.getUserById(0)).thenReturn(Optional.empty());

        MockHttpServletRequest request = new MockHttpServletRequest();

        Assertions.assertThrows(NoSuchElementException.class, () -> reviewService.addReview(reviewRequestDTO, request));
    }

    @Test
    void addReviewWithUserButWithoutDrinkingFountain() {
        // Attributes for review
        String text = "My first drinking fountain review";
        int stars = 4;
        DrinkingFountainEntity.FountainType type = DrinkingFountainEntity.FountainType.DRINKING;
        int drinkingFountainId = 0;

        // Attributes for user
        int id = 0;
        String name = "Mihusu";
        String password = "Jeff112233";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        ZonedDateTime specificCreatedAtUser = ZonedDateTime.parse("2023-01-01T00:00:00.000000+01:00[Europe/Copenhagen]");

        UserEntity user = UserEntity.builder()
                .id(id)
                .name(name)
                .password(hashedPassword)
                .role(UserEntity.RoleType.USER)
                .createdAt(specificCreatedAtUser)
                .build();

        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(text, stars, type, drinkingFountainId);
        Mockito.when(loginService.getUserById(0)).thenReturn(Optional.of(user));

        MockHttpServletRequest request = new MockHttpServletRequest();

        Assertions.assertThrows(NoSuchElementException.class, () -> reviewService.addReview(reviewRequestDTO, request));
    }

    @Test
    void addReviewWithUserAndDrinkingFountain() {
        // Attributes for review
        String text = "My first drinking fountain review";
        int stars = 4;
        DrinkingFountainEntity.FountainType type = DrinkingFountainEntity.FountainType.DRINKING;
        int drinkingFountainId = 0;
        ZonedDateTime specificCreatedAt = ZonedDateTime.parse("2023-01-01T00:00:00.000000+01:00[Europe/Copenhagen]");

        // Attributes for user
        int id = 0;
        String name = "Mihusu";
        String password = "Jeff112233";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        //Attributes for fountain
        double latitude = 232.3232232;
        double longitude = 53463.3552;
        double score = 4;

        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            mockedStatic.when(ZonedDateTime::now).thenReturn(specificCreatedAt);

            UserEntity user = UserEntity.builder()
                    .id(id)
                    .name(name)
                    .password(hashedPassword)
                    .role(UserEntity.RoleType.USER)
                    .createdAt(specificCreatedAt)
                    .build();

            // Adding a String to the list
            ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(text, stars, type, drinkingFountainId);
            Mockito.when(loginService.getUserById(0)).thenReturn(Optional.of(user));

            DrinkingFountainEntity fountainEntity = DrinkingFountainEntity.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .type(type)
                    .createdAt(specificCreatedAt)
                    .approved(false)
                    .score(score)
                    .fountainImageEntities(List.of())
                    .reviewEntities(List.of())
                    .build();

            Mockito.when(drinkingFountainRepository.findById(reviewRequestDTO.drinkingFountainId())).thenReturn(Optional.of(fountainEntity));

            ReviewsScoreSumProjection mockedProjection = ReviewsScoreSumProjectionMock.createMock(6, 2);
            Mockito.when(reviewRepository.getReviewSumAndCount(id)).thenReturn(mockedProjection);

            ReviewEntity reviewEntity = ReviewEntity.builder()
                    .text(reviewRequestDTO.text())
                    .stars(reviewRequestDTO.stars())
                    .type(reviewRequestDTO.type())
                    .userEntity(user)
                    .createdAt(specificCreatedAt)
                    .drinkingFountain(fountainEntity)
                    .build();

            Mockito.when(reviewRepository.save(Mockito.any())).thenReturn(reviewEntity);

            MockHttpServletRequest request = new MockHttpServletRequest();
            reviewService.addReview(reviewRequestDTO, request);

            Mockito.verify(reviewRepository, Mockito.times(1)).save(reviewEntity);
        }
    }
}
