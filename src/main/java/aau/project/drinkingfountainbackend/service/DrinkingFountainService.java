package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.*;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainEntity;
import aau.project.drinkingfountainbackend.persistence.entity.DrinkingFountainImageEntity;
import aau.project.drinkingfountainbackend.persistence.projection.DrinkingFountainListViewProjection;
import aau.project.drinkingfountainbackend.persistence.projection.DrinkingFountainMapProjection;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainImageRepository;
import aau.project.drinkingfountainbackend.persistence.repository.DrinkingFountainRepository;
import aau.project.drinkingfountainbackend.util.Base64Utility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DrinkingFountainService {

    private final DrinkingFountainRepository drinkingFountainRepository;
    private final DrinkingFountainImageRepository drinkingFountainImageRepository;
    private final ReviewService reviewService;

    @Autowired
    public DrinkingFountainService(DrinkingFountainRepository drinkingFountainRepository, DrinkingFountainImageRepository drinkingFountainImageRepository, ReviewService reviewService) {
        this.drinkingFountainRepository = drinkingFountainRepository;
        this.drinkingFountainImageRepository = drinkingFountainImageRepository;
        this.reviewService = reviewService;
    }

    public Optional<DrinkingFountainDTO> getDrinkingFountain(int id){
        Optional<DrinkingFountainEntity> drinkingFountainEntity = drinkingFountainRepository.getFirstByIdAndApproved(id, true);
        return drinkingFountainEntity.flatMap(entity -> Optional.of(drinkingFountainDTOMapper(entity)));
    }

    public List<DrinkingFountainMapDTO> getDrinkingFountainMapData(double latitude, double longitude){
        List<DrinkingFountainMapProjection> drinkingFountainMapProjections = drinkingFountainRepository.findAllByApprovedMapped(latitude, longitude, true);
        return drinkingFountainMapProjections.stream().map(
                entity -> new DrinkingFountainMapDTO(entity.getId(), entity.getLatitude(), entity.getLongitude(), entity.getDistance())
        ).collect(Collectors.toList());
    }

    @Transactional
    public void saveDrinkingFountainRequest(DrinkingFountainRequestDTO drinkingFountainRequestDTO, HttpServletRequest httpServletRequest) {
        DrinkingFountainEntity savedDrinkingFountain = drinkingFountainRepository.save(DrinkingFountainEntity.builder()
                .latitude(drinkingFountainRequestDTO.latitude())
                .longitude(drinkingFountainRequestDTO.longitude())
                .type(drinkingFountainRequestDTO.type())
                .score(drinkingFountainRequestDTO.score())
                .createdAt(ZonedDateTime.now())
                .approved(false)
                .score(drinkingFountainRequestDTO.score())
                .build()
        );

        drinkingFountainImageRepository.save(DrinkingFountainImageEntity.builder()
                .drinkingFountain(savedDrinkingFountain)
                .image(Base64Utility.decode(drinkingFountainRequestDTO.base64Images()))
                .createdAt(ZonedDateTime.now())
                .build());

        reviewService.addReview(new ReviewRequestDTO(
                drinkingFountainRequestDTO.review(),
                drinkingFountainRequestDTO.score(),
                List.of(),
                drinkingFountainRequestDTO.type(),
                savedDrinkingFountain.getId()
        ), httpServletRequest);
    }

    public void approveDrinkingFountain(int id) {
        drinkingFountainRepository.approveById(id);
    }

    public void unapproveDrinkingFountain(int id) {
        drinkingFountainRepository.deleteById(id);
    }

    public List<DrinkingFountainDTO> getUnapprovedDrinkingFountains() {
        List<DrinkingFountainEntity> unapproved = drinkingFountainRepository.findAllByApprovedEntity(false);
        return unapproved.stream().map(this::drinkingFountainDTOMapper).collect(Collectors.toList());
    }

    private DrinkingFountainDTO drinkingFountainDTOMapper(DrinkingFountainEntity entity) {
        List<ReviewDTO> reviewDTOS = entity.getReviewEntities().stream().map(reviewEntity -> {
            List<ReviewImageDTO> reviewImages = reviewEntity.getReviewImages().stream().map(
                    reviewImageEntity -> new ReviewImageDTO(Base64Utility.encode(reviewImageEntity.getImage()))).toList();
            return new ReviewDTO(
                    reviewEntity.getText(),
                    reviewEntity.getStars(),
                    reviewImages,
                    reviewEntity.getType(),
                    reviewEntity.getUserEntity().getName(),
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

    public List<FountainListViewDTO> getNearestDrinkingFountains(double latitude, double longitude) {
        Pageable pageRequest = PageRequest.of(0, 5);
        boolean approvedStatus = true;
        List<DrinkingFountainListViewProjection> projectionList = drinkingFountainRepository.findNearestFountains(latitude, longitude, approvedStatus, pageRequest);
        return projectionList.stream().map(this::fountainListViewDTOMapper).toList();
    }

    private FountainListViewDTO fountainListViewDTOMapper(DrinkingFountainListViewProjection projection) {
        return new FountainListViewDTO(projection.getId(), projection.getDistance(), projection.getLatitude(), projection.getLongitude(), projection.getType(), projection.getScore());
    }
}