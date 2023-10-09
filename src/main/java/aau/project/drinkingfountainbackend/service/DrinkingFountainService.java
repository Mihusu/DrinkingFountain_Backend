package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.*;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.projection.DrinkingFountainMapProjection;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import aau.project.drinkingfountainbackend.util.Base64Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
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
        List<ReviewDTO> reviewDTOS =  entity.getReviewEntities().stream().map(reviewEntity -> {

                List<ReviewImageDTO> reviewImages = reviewEntity.getReviewImageEntities().stream().map(
                        reviewImageEntity -> new ReviewImageDTO(Base64Utility.encode(reviewImageEntity.getImage()))).toList();

            return new ReviewDTO(
                    reviewEntity.getText(),
                    reviewEntity.getStars(),
                    reviewImages,
                    "test",
                    reviewEntity.getType(),
                    reviewEntity.getCreatedAt());
        }).toList();

        List<FountainImageDTO> fountainImageDTOS = entity.getFountainImageEntities().stream().map(
                        //Turn each entity into a DTO
                        image -> new FountainImageDTO(
                                //From byte[] to base64 String
                                Base64Utility.encode(image.getImage())
                        )).collect(Collectors.toList());

        return new DrinkingFountainDTO(
                entity.getId(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getType(),
                entity.getCreatedAt(),
                entity.getScore(),
                fountainImageDTOS,
                reviewDTOS);
    }

    public Optional<DrinkingFountainEntity> getDrinkingFountainEntity(int i) {
        return drinkingFountainRepository.findById(i);
    }
}
