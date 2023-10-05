package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainRequestDTO;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class DrinkingFountainService {

    DrinkingFountainRepository drinkingFountainRepository;

    @Autowired
    public DrinkingFountainService(DrinkingFountainRepository drinkingFountainRepository) {
        this.drinkingFountainRepository = drinkingFountainRepository;
    }

    public Optional<DrinkingFountainDTO> getDrinkingFountain(int id){
        Optional<DrinkingFountainEntity> drinkingFountainEntity = drinkingFountainRepository.getFirstById(id);

        return drinkingFountainEntity.flatMap(entity -> Optional.of(new DrinkingFountainDTO(
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getType(),
                entity.getCreatedAt(),
                entity.getScore()
        )));
    }

    public void saveDrinkingFountainRequest(DrinkingFountainRequestDTO drinkingFountainRequestDTO) {
        DrinkingFountainEntity savedResult = drinkingFountainRepository.save(DrinkingFountainEntity.builder()
                .latitude(drinkingFountainRequestDTO.latitude())
                .longitude(drinkingFountainRequestDTO.longitude())
                .type(drinkingFountainRequestDTO.type())
                .score(drinkingFountainRequestDTO.score())
                .createdAt(ZonedDateTime.now())
                .approved(false)
                .score(drinkingFountainRequestDTO.score())
                .build()
        );
        System.out.println(savedResult.getId());
    }
}
