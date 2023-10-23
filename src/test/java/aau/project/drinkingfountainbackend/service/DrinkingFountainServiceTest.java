package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainMapDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainRequestDTO;
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

    @Test
    void getDrinkingFountainWithDataTest(){
        double score = 2;

        //When the db is called mock it to return an entity
        Mockito.when(drinkingFountainRepository.getFirstByIdAndApproved(1, true))
                .thenReturn(Optional.of(DrinkingFountainEntity.builder().id(1).latitude(1.).longitude(1.).reviewEntities(List.of()).approved(true).fountainImageEntities(List.of()).score(score).build()));

        Optional<DrinkingFountainDTO> fountain = drinkingFountainService.getDrinkingFountain(1);

        //Check the DTO is present and have the expected score value
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

        Mockito.when(drinkingFountainRepository.findAllByApprovedMapped(true))
                .thenReturn(List.of());

        List<DrinkingFountainMapDTO> mapData = drinkingFountainService.getDrinkingFountainMapData();

        Assertions.assertTrue(mapData.isEmpty());
    }

    @Test
    void saveDrinkingFountainRequestTest() {
        //Attributes
        double latitude = 232.3232232;
        double longitude = 53463.3552;
        double score = 4;
        DrinkingFountainEntity.FountainType type = DrinkingFountainEntity.FountainType.DRINKING;
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

            //Test
            DrinkingFountainRequestDTO drinkingFountainRequestDTO = new DrinkingFountainRequestDTO(latitude, longitude, type, score, base64);
            drinkingFountainService.saveDrinkingFountainRequest(drinkingFountainRequestDTO);

            //Assertions
            Mockito.verify(drinkingFountainImageRepository, Mockito.times(1)).save(expectedFountainImageToBeSaved);
            Mockito.verify(drinkingFountainRepository, Mockito.times(1)).save(expectedFountainEntityToBeSaved);
        }
    }

}
