package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainDTO;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainImageRepository;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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
}
