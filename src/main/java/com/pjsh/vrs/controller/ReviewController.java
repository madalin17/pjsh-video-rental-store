package com.pjsh.vrs.controller;

import com.pjsh.vrs.controller.requests.ReviewRequest;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.CustomerService;
import com.pjsh.vrs.service.ReviewService;
import com.pjsh.vrs.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Review addReview(@RequestBody ReviewRequest request) {
        Video video = videoService.getByTitle(request.getTitle());
        if (video == null) {
            throw new IllegalArgumentException("Video not found");
        }

        Customer customer = customerService.getByUsername(request.getUsername());
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        Review review = new Review(video, customer, request.getDescription());
        return reviewService.addReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/video/{videoId}")
    public List<Review> findByVideoId(@PathVariable Long videoId) {
        return reviewService.findByVideoId(videoId);
    }

    @GetMapping("/customer/{customerId}")
    public List<Review> findByCustomerId(@PathVariable Long customerId) {
        return reviewService.findByCustomerId(customerId);
    }

    @DeleteMapping("/video/all/{videoId}")
    public void deleteAllByVideoId(@PathVariable Long videoId) {
        reviewService.deleteAllByVideoId(videoId);
    }

    @DeleteMapping("/customer/all/{customerId}")
    public void deleteAllByCustomerId(@PathVariable Long customerId) {
        reviewService.deleteAllByCustomerId(customerId);
    }
}

