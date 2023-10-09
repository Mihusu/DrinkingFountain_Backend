package aau.project.drinkingfountainbackend.service;

import aau.project.drinkingfountainbackend.api.dto.ReviewDTO;
import aau.project.drinkingfountainbackend.persistence.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
