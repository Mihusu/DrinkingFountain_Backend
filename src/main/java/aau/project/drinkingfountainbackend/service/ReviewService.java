package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.FountainImageDTO;
import aau.project.drinkingfountainbackend.api.dto.ReviewDTO;
import aau.project.drinkingfountainbackend.persistence.entity.FountainImageEntity;
import aau.project.drinkingfountainbackend.persistence.entity.ReviewEntity;
import aau.project.drinkingfountainbackend.persistence.entity.ReviewImageEntity;
import aau.project.drinkingfountainbackend.persistence.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void addReview(ReviewDTO reviewDTO) {

    }

    public void deleteReview(int id) {

    }

    public void editReview(ReviewDTO reviewDTO) {

    }
}
