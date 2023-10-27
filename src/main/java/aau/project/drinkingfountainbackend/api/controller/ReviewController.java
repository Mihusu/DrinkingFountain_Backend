package aau.project.drinkingfountainbackend.api.controller;

import aau.project.drinkingfountainbackend.api.dto.ReviewRequestDTO;
import aau.project.drinkingfountainbackend.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController()
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody ReviewRequestDTO reviewRequestDTO, HttpServletRequest httpServletRequest) {
        try {
            reviewService.addReview(reviewRequestDTO, httpServletRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
