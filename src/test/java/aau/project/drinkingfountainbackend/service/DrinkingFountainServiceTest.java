package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainMapDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainRequestDTO;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainImageEntity;
import aau.project.drinkingfountainbackend.persistence.projection.DrinkingFountainMapProjection;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainImageRepository;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import aau.project.drinkingfountainbackend.util.Base64Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
        double latitude = 232.3232232;
        double longitude = 53463.3552;

        // Define a specific instant and time zone for your test
        Instant fixedInstant = Instant.parse("2023-10-23T12:00:00Z");
        ZoneId fixedTimeZone = ZoneId.of("UTC-1");

        // Create a Clock instance with the fixed instant and time zone
        Clock fixedClock = Clock.fixed(fixedInstant, fixedTimeZone);

        ZonedDateTime expectedZonedDateTime = ZonedDateTime.ofInstant(fixedInstant, fixedTimeZone);
        ZonedDateTime mockZonedDateTime = Mockito.mock(ZonedDateTime.class);
        Mockito.when(ZonedDateTime.now(fixedClock)).thenReturn(expectedZonedDateTime);

        DrinkingFountainRequestDTO drinkingFountainRequestDTO = new DrinkingFountainRequestDTO(latitude, longitude,
                DrinkingFountainEntity.FountainType.DRINKING, 4, "redawr");

        drinkingFountainService.saveDrinkingFountainRequest(drinkingFountainRequestDTO);

        DrinkingFountainEntity expectedFountainEntityToBeSaved = DrinkingFountainEntity.builder()
                .latitude(latitude)
                .longitude(longitude)
                .type(DrinkingFountainEntity.FountainType.DRINKING)
                .createdAt(ZonedDateTime.now(fixedClock))
                .approved(false)
                .score(4)
                .reviewEntities(List.of())
                .build();

        DrinkingFountainImageEntity expectedFountainImageToBeSaved = DrinkingFountainImageEntity.builder()
                .image(Base64Utility.decode(drinkingFountainRequestDTO.base64Images()))
                .createdAt(ZonedDateTime.now(fixedClock))
                .drinkingFountain(expectedFountainEntityToBeSaved)
                .build();

        Mockito.verify(drinkingFountainImageRepository, Mockito.times(1)).save(expectedFountainImageToBeSaved);
    }
}
