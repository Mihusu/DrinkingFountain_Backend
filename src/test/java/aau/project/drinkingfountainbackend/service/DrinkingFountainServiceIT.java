package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.EnableTestcontainers;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainMapDTO;
import aau.project.drinkingfountainbackend.api.dto.FountainListViewDTO;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainImageRepository;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import aau.project.drinkingfountainbackend.persistence.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.Option;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@EnableTestcontainers
public class DrinkingFountainServiceIT {
    @Autowired
    DrinkingFountainRepository drinkingFountainRepository;
    @Autowired
    DrinkingFountainImageRepository drinkingFountainImageRepository;
    @Autowired
    DrinkingFountainService drinkingFountainService;


    @Transactional
    @Test
    void getDrinkingFountainIT() {
        DrinkingFountainEntity drinkingFountainEntity = DrinkingFountainEntity.builder()
                .id(1234)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(123456)
                .longitude(123456)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
        .build();

        drinkingFountainRepository.save(drinkingFountainEntity);

        Optional<DrinkingFountainDTO> drinkingFountainDTO = drinkingFountainService.getDrinkingFountain(drinkingFountainEntity.getId());

        Assertions.assertTrue(drinkingFountainDTO.isPresent());
        Assertions.assertEquals(drinkingFountainDTO.get().id(), drinkingFountainEntity.getId());
    }

    @Transactional
    @Test
    void getDrinkingFountainMapDataIT() {
        DrinkingFountainEntity drinkingFountainEntity1 = DrinkingFountainEntity.builder()
                .id(1234)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(123456)
                .longitude(123456)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();
        DrinkingFountainEntity drinkingFountainEntity2 = DrinkingFountainEntity.builder()
                .id(5678)
                .score(1)
                .type(DrinkingFountainEntity.FountainType.valueOf("FILLING"))
                .latitude(123456)
                .longitude(123456)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        drinkingFountainRepository.saveAll(List.of(drinkingFountainEntity1, drinkingFountainEntity2));

        List<DrinkingFountainMapDTO> drinkingFountainMapDTOSList = drinkingFountainService.getDrinkingFountainMapData(123456, 123456);

        Assertions.assertEquals(2, drinkingFountainMapDTOSList.size());

        DrinkingFountainMapDTO drinkingFountainMapDTO1 = drinkingFountainMapDTOSList.get(0);
        Assertions.assertEquals(drinkingFountainMapDTO1.id(), drinkingFountainEntity1.getId());

        DrinkingFountainMapDTO drinkingFountainMapDTO2 = drinkingFountainMapDTOSList.get(1);
        Assertions.assertEquals(drinkingFountainMapDTO2.id(), drinkingFountainEntity2.getId());
    }

    @Transactional
    @Test
    void saveDrinkingFountainRequestIT() {

        DrinkingFountainDTO drinkingFountainDTO = new DrinkingFountainDTO(
                1234,
                1234.5678,
                1234.5678,
                DrinkingFountainEntity.FountainType.valueOf("DRINKING"),
                ZonedDateTime.now(),
                5.0,
                List.of(),
                List.of()
        );

        // TODO: pass HttpServletRequest
        //drinkingFountainService.saveDrinkingFountainRequest(drinkingFountainDTO, /*httpServletRequest*/);

        Optional<DrinkingFountainEntity> drinkingFountainEntity = drinkingFountainRepository.getFirstByIdAndApproved(drinkingFountainDTO.id(), true);

        Assertions.assertTrue(drinkingFountainEntity.isPresent());
    }

    @Transactional
    @Test
    void approveDrinkingFountainIT() {
        DrinkingFountainEntity drinkingFountainEntity = DrinkingFountainEntity.builder()
                .id(1234)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(123456)
                .longitude(123456)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        drinkingFountainRepository.save(drinkingFountainEntity);

        drinkingFountainService.approveDrinkingFountain(drinkingFountainEntity.getId());

        Optional<DrinkingFountainEntity> fetchedDrinkingFountainEntity = drinkingFountainRepository.getFirstByIdAndApproved(drinkingFountainEntity.getId(), true);

        // It is only present if it is approved I think?
        Assertions.assertTrue(fetchedDrinkingFountainEntity.isPresent());
    }

    @Transactional
    @Test
    void unapproveDrinkingFountainIT() {
        DrinkingFountainEntity drinkingFountainEntity = DrinkingFountainEntity.builder()
                .id(1234)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(123456)
                .longitude(123456)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        drinkingFountainRepository.save(drinkingFountainEntity);

        drinkingFountainService.unapproveDrinkingFountain(drinkingFountainEntity.getId());

        Optional<DrinkingFountainEntity> fetchedDrinkingFountainEntity = drinkingFountainRepository.getFirstByIdAndApproved(drinkingFountainEntity.getId(), true);

        Assertions.assertFalse(fetchedDrinkingFountainEntity.isPresent());
    }

    @Transactional
    @Test
    void getUnapprovedDrinkingFountainsIT() {
        DrinkingFountainEntity drinkingFountainEntity = DrinkingFountainEntity.builder()
                .id(1234)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(123456)
                .longitude(123456)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        drinkingFountainRepository.save(drinkingFountainEntity);

        List<DrinkingFountainDTO> unapprovedDrinkingFountainDTOs = drinkingFountainService.getUnapprovedDrinkingFountains();

        Assertions.assertEquals(unapprovedDrinkingFountainDTOs.get(0).id(), drinkingFountainEntity.getId());
        Assertions.assertEquals(1, unapprovedDrinkingFountainDTOs.size());
    }

    @Transactional
    @Test
    void getNearestDrinkingFountainsIT() {

        List<DrinkingFountainEntity> drinkingFountainEntityList;

        double testLatitude = 50.0000;
        double testLongitude = 20.0000;

        DrinkingFountainEntity drinkingFountainEntity1 = DrinkingFountainEntity.builder()
                .id(1)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0001)
                .longitude(testLongitude + 0.0001)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity drinkingFountainEntity2 = DrinkingFountainEntity.builder()
                .id(2)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0002)
                .longitude(testLongitude + 0.0002)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity drinkingFountainEntity3 = DrinkingFountainEntity.builder()
                .id(3)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0003)
                .longitude(testLongitude + 0.0003)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity drinkingFountainEntity4 = DrinkingFountainEntity.builder()
                .id(4)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0004)
                .longitude(testLongitude + 0.0004)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity drinkingFountainEntity5 = DrinkingFountainEntity.builder()
                .id(5)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0005)
                .longitude(testLongitude + 0.0005)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity drinkingFountainEntity6 = DrinkingFountainEntity.builder()
                .id(6)
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0006)
                .longitude(testLongitude + 0.0006)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        drinkingFountainRepository.saveAll(Arrays.asList(
                drinkingFountainEntity1,
                drinkingFountainEntity2,
                drinkingFountainEntity3,
                drinkingFountainEntity4,
                drinkingFountainEntity5,
                drinkingFountainEntity6));

        List<FountainListViewDTO> fountainListViewDTOS = drinkingFountainService.getNearestDrinkingFountains(testLatitude, testLongitude);

        Assertions.assertEquals(5, fountainListViewDTOS.size());
        Assertions.assertFalse(fountainListViewDTOS.contains(drinkingFountainRepository.getFirstByIdAndApproved(6, true)));

    }

}
