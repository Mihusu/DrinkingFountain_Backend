package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainMapDTO;
import aau.project.drinkingfountainbackend.api.dto.DrinkingFountainRequestDTO;
import aau.project.drinkingfountainbackend.api.dto.FountainImageDTO;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainImageEntity;
import aau.project.drinkingfountainbackend.persistence.projection.DrinkingFountainMapProjection;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DrinkingFountainService {

    DrinkingFountainRepository drinkingFountainRepository;

    @Autowired
    public DrinkingFountainService(DrinkingFountainRepository drinkingFountainRepository) {
        this.drinkingFountainRepository = drinkingFountainRepository;
    }

    public Optional<DrinkingFountainDTO> getDrinkingFountain(int id){
        Optional<DrinkingFountainEntity> drinkingFountainEntity = drinkingFountainRepository.getFirstByIdAndApproved(id, true);
        return drinkingFountainEntity.flatMap(entity -> Optional.of(drinkingFountainDTOMapper(entity)));
    }

    public List<DrinkingFountainMapDTO> getDrinkingFountainMapData(){
        List<DrinkingFountainMapProjection> drinkingFountainMapProjections = drinkingFountainRepository.findAllByApprovedMapped(true);
        return drinkingFountainMapProjections.stream().map(
                entity -> new DrinkingFountainMapDTO(entity.getId(),entity.getLatitude(), entity.getLongitude())
        ).collect(Collectors.toList());
    }

    public void saveDrinkingFountainRequest(DrinkingFountainRequestDTO drinkingFountainRequestDTO) {
        drinkingFountainRepository.save(DrinkingFountainEntity.builder()
                .latitude(drinkingFountainRequestDTO.latitude())
                .longitude(drinkingFountainRequestDTO.longitude())
                .type(drinkingFountainRequestDTO.type())
                .score(drinkingFountainRequestDTO.score())
                .createdAt(ZonedDateTime.now())
                .approved(false)
                .score(drinkingFountainRequestDTO.score())
                .build()
        );
    }

    public void approveDrinkingFountain(int id) {
        drinkingFountainRepository.approveById(id);
    }

    public List<DrinkingFountainDTO> getUnapprovedDrinkingFountains() {
        List<DrinkingFountainEntity> unapproved = drinkingFountainRepository.findAllByApprovedEntity(false);
        return unapproved.stream().map(this::drinkingFountainDTOMapper).collect(Collectors.toList());
    }

    private DrinkingFountainDTO drinkingFountainDTOMapper (DrinkingFountainEntity entity){
        List<DrinkingFountainImageEntity> images = entity.getFountainImageEntities();

        List<FountainImageDTO> fountainImageDTOS = images.stream().map(
                        //Turn each entity into a DTO
                        image -> new FountainImageDTO(
                                //From byte[] to base64 String
                                Base64.getEncoder().encodeToString(image.getImage())))
                .collect(Collectors.toList());

        return new DrinkingFountainDTO(
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getType(),
                entity.getCreatedAt(),
                entity.getScore(),
                fountainImageDTOS);
    }
}
