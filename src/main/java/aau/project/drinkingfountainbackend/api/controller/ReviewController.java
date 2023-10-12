package aau.project.drinkingfountainbackend.api.controller;

import aau.project.drinkingfountainbackend.api.dto.ReviewRequestDTO;
import aau.project.drinkingfountainbackend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/review")
public class ReviewController {

    ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PostMapping("/create")
    public void createReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        reviewService.addReview(reviewRequestDTO);
    }

}
