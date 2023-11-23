package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.*;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainImageEntity;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainImageRepository;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import aau.project.drinkingfountainbackend.util.Base64Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class DrinkingFountainServiceTest {

    @InjectMocks
    private DrinkingFountainService drinkingFountainService;

    @Mock
    private DrinkingFountainRepository drinkingFountainRepository;

    @Mock
    private DrinkingFountainImageRepository drinkingFountainImageRepository;

    @Mock
    private ReviewService reviewService;

    @Test
    void getDrinkingFountainWithDataTest(){
        double score = 2;

        //When the db is called mock it to return an entity
        Mockito.when(drinkingFountainRepository.getFirstByIdAndApproved(1, true))
                .thenReturn(Optional.of(DrinkingFountainEntity.builder().id(1).latitude(1.).longitude(1.).reviewEntities(List.of()).approved(true).fountainImageEntities(List.of()).score(score).build()));

        Optional<DrinkingFountainDTO> fountain = drinkingFountainService.getDrinkingFountain(1);

        //Check the DTO is present and have the expected score value
        Assertions.assertTrue(fountain.isPresent());
        Assertions.assertEquals(score, fountain.get().score());
    }

    @Test
    void getDrinkingFountainWithoutDataTest(){
        //When the db is called mock it to return an entity
        Mockito.when(drinkingFountainRepository.getFirstByIdAndApproved(1, true))
                .thenReturn(Optional.empty());

        Optional<DrinkingFountainDTO> fountain = drinkingFountainService.getDrinkingFountain(1);

        //Check the DTO is present and have the expected score value
        Assertions.assertTrue(fountain.isEmpty());
    }

    @Test
    void getDrinkingFountainMapDataTest() {
        Mockito.when(drinkingFountainRepository.findAllByApprovedMapped(10., 50,true))
                .thenReturn(List.of());

        List<DrinkingFountainMapDTO> mapData = drinkingFountainService.getDrinkingFountainMapData(10.,50);

        Assertions.assertTrue(mapData.isEmpty());
    }

    @Test
    void approveDrinkingFountainTest() {
        int id = 2;

        // Call the service method
        drinkingFountainService.approveDrinkingFountain(id);

        Mockito.verify(drinkingFountainRepository, Mockito.times(1)).approveById(id);
    }

    @Test
    void unapproveDrinkingFountainTest() {
        int id = 2;

        // Call the service method
        drinkingFountainService.unapproveDrinkingFountain(id);

        Mockito.verify(drinkingFountainRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void getUnapprovedDrinkingFountainsWithDataTest() {
        //Attributes
        double latitude = 232.3232232;
        double longitude = 53463.3552;
        int score = 4;
        DrinkingFountainEntity.FountainType type = DrinkingFountainEntity.FountainType.DRINKING;
        String review = "Review";
        String base64 = "redawr";
        ZonedDateTime specificCreatedAt = ZonedDateTime.parse("2023-01-01T00:00:00.000000+01:00[Europe/Copenhagen]");

        // Mocking data
        DrinkingFountainEntity unapprovedFountainEntity = DrinkingFountainEntity.builder()
                .latitude(latitude)
                .longitude(longitude)
                .type(type)
                .createdAt(specificCreatedAt)
                .approved(false)
                .score(score)
                .reviewEntities(List.of())
                .fountainImageEntities(List.of())
                .build();

        DrinkingFountainRequestDTO unapprovedFountainDTO = new DrinkingFountainRequestDTO(latitude, longitude, type, review, score, base64);

        // Set DTO properties accordingly if needed
        Mockito.when(drinkingFountainRepository.findAllByApprovedEntity(false))
                .thenReturn(List.of(unapprovedFountainEntity));

        // Call the method being tested
        List<DrinkingFountainDTO> unapprovedFountains = drinkingFountainService.getUnapprovedDrinkingFountains();

        // Assertions
        Assertions.assertNotNull(unapprovedFountains);
        Assertions.assertEquals(1, unapprovedFountains.size());
        Assertions.assertEquals(unapprovedFountainDTO.score(), unapprovedFountains.get(0).score());

        // Optionally, verify if repository method and DTO mapper were called
        Mockito.verify(drinkingFountainRepository, Mockito.times(1)).findAllByApprovedEntity(false);
    }

    @Test
    void drinkingFountainDTOMapperTest() {
        //Attributes
        double latitude = 232.3232232;
        double longitude = 53463.3552;
        int score = 4;
        DrinkingFountainEntity.FountainType type = DrinkingFountainEntity.FountainType.DRINKING;
        String review = "Review";
        String base64 = "redawr";
        ZonedDateTime specificCreatedAt = ZonedDateTime.parse("2023-01-01T00:00:00.000000+01:00[Europe/Copenhagen]");

        try (MockedStatic<ZonedDateTime> mockedStatic = mockStatic(ZonedDateTime.class)) {
            mockedStatic.when(ZonedDateTime::now).thenReturn(specificCreatedAt);

            //Entities for mocking and verification
            DrinkingFountainEntity expectedFountainEntityToBeSaved = DrinkingFountainEntity.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .type(type)
                    .createdAt(specificCreatedAt)
                    .approved(false)
                    .score(score)
                    .build();

            DrinkingFountainImageEntity expectedFountainImageToBeSaved = DrinkingFountainImageEntity.builder()
                    .image(Base64Utility.decode(base64))
                    .createdAt(specificCreatedAt)
                    .drinkingFountain(expectedFountainEntityToBeSaved)
                    .build();

            //Mocking
            Mockito.when(drinkingFountainRepository.save(expectedFountainEntityToBeSaved)).thenReturn(expectedFountainEntityToBeSaved);

            // Create DTO and call method
            DrinkingFountainRequestDTO drinkingFountainRequestDTO = new DrinkingFountainRequestDTO(latitude, longitude, type, review, score, base64);

            MockHttpServletRequest request = new MockHttpServletRequest();
            drinkingFountainService.saveDrinkingFountainRequest(drinkingFountainRequestDTO, request);

            //Assertions
            Mockito.verify(drinkingFountainImageRepository, Mockito.times(1)).save(expectedFountainImageToBeSaved);
            Mockito.verify(drinkingFountainRepository, Mockito.times(1)).save(expectedFountainEntityToBeSaved);
        }
    }

    @Test
    void getNearestDrinkingFountains() {
        //Attributes
        double latitude = 232.3232232;
        double longitude = 53463.3552;
        int score = 4;
        DrinkingFountainEntity.FountainType type = DrinkingFountainEntity.FountainType.DRINKING;
        ZonedDateTime specificCreatedAt = ZonedDateTime.parse("2023-01-01T00:00:00.000000+01:00[Europe/Copenhagen]");

        // Mocking data
        DrinkingFountainEntity fountainEntity = DrinkingFountainEntity.builder()
                .latitude(latitude)
                .longitude(longitude)
                .type(type)
                .createdAt(specificCreatedAt)
                .approved(false)
                .score(score)
                .reviewEntities(List.of())
                .fountainImageEntities(List.of())
                .build();

        drinkingFountainService.getNearestDrinkingFountains(fountainEntity.getLatitude(), fountainEntity.getLongitude());
    }
}
