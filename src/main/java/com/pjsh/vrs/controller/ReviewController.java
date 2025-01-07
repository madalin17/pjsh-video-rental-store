package com.pjsh.vrs.controller;

import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/video/{videoId}")
    public List<Review> getReviewsByVideoId(@PathVariable Long videoId) {
        return reviewService.getReviewsByVideoId(videoId);
    }

    @GetMapping("/customer/{customerId}")
    public List<Review> getReviewsByCustomerId(@PathVariable Long customerId) {
        return reviewService.getReviewsByCustomerId(customerId);
    }

    @PostMapping
    public Review addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}

