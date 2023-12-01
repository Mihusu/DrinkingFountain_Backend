package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.EnableTestcontainers;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainMapDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainRequestDTO;
import aau.project.drinkingfountainbackend.api.dto.FountainListViewDTO;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.entity.UserEntity;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainImageRepository;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import aau.project.drinkingfountainbackend.persistence.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.*;


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
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenService jwtTokenService;

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

        DrinkingFountainRequestDTO drinkingFountainRequestDTO = new DrinkingFountainRequestDTO(
                1234.5678,
                1234.5678,
                DrinkingFountainEntity.FountainType.valueOf("DRINKING"),
                "",
                5,
                "");

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

        drinkingFountainService.saveDrinkingFountainRequest(drinkingFountainRequestDTO, mockHttpServletRequest);

        List<DrinkingFountainEntity> drinkingFountainEntityList = drinkingFountainRepository.findAll();
        DrinkingFountainEntity drinkingFountainEntity = drinkingFountainEntityList.get(0);

        Assertions.assertEquals(1, drinkingFountainEntityList.size());
        Assertions.assertEquals(drinkingFountainRequestDTO.latitude(), drinkingFountainEntity.getLatitude());
        Assertions.assertEquals(drinkingFountainRequestDTO.longitude(), drinkingFountainEntity.getLongitude());
        Assertions.assertEquals(drinkingFountainRequestDTO.score(), drinkingFountainEntity.getScore());
        Assertions.assertEquals(drinkingFountainRequestDTO.type(), drinkingFountainEntity.getType());
    }

    @Transactional
    @Test
    void approveDrinkingFountainIT() {
        DrinkingFountainEntity drinkingFountainEntity = DrinkingFountainEntity.builder()
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
                .approved(false)
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

        double testLatitude = 50.0000;
        double testLongitude = 20.0000;

        DrinkingFountainEntity df1 = DrinkingFountainEntity.builder()
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0001)
                .longitude(testLongitude + 0.0001)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity df2 = DrinkingFountainEntity.builder()
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0002)
                .longitude(testLongitude + 0.0002)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity df3 = DrinkingFountainEntity.builder()
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0003)
                .longitude(testLongitude + 0.0003)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity df4 = DrinkingFountainEntity.builder()
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0004)
                .longitude(testLongitude + 0.0004)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity df5 = DrinkingFountainEntity.builder()
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0005)
                .longitude(testLongitude + 0.0005)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        DrinkingFountainEntity df6 = DrinkingFountainEntity.builder()
                .score(5)
                .type(DrinkingFountainEntity.FountainType.valueOf("DRINKING"))
                .latitude(testLatitude + 0.0006)
                .longitude(testLongitude + 0.0006)
                .createdAt(ZonedDateTime.now())
                .approved(true)
                .fountainImageEntities(List.of())
                .reviewEntities(List.of())
                .build();

        System.out.print("There are ");
        System.out.print(drinkingFountainService.getNearestDrinkingFountains(testLatitude, testLongitude).size());
        System.out.print(" fountains in the repository");
        System.out.println();

        drinkingFountainRepository.saveAll(Arrays.asList(df1, df2, df3, df4, df5, df6));

        System.out.print("There are ");
        System.out.print(drinkingFountainService.getNearestDrinkingFountains(testLatitude, testLongitude).size());
        System.out.print(" fountains in the repository");
        System.out.println();


        List<FountainListViewDTO> fountainListViewDTOS = drinkingFountainService.getNearestDrinkingFountains(testLatitude, testLongitude);

        Assertions.assertEquals(5, fountainListViewDTOS.size());
        Assertions.assertEquals(fountainListViewDTOS.get(0).id(), df1.getId());
        Assertions.assertFalse(fountainListViewDTOS.contains(drinkingFountainRepository.getFirstByIdAndApproved(6, true)));
    }


}
