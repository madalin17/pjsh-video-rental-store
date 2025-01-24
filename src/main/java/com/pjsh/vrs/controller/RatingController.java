package com.pjsh.vrs.controller;

import com.pjsh.vrs.controller.requests.RatingRequest;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.CustomerService;
import com.pjsh.vrs.service.RatingService;
import com.pjsh.vrs.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public Rating addRating(@RequestBody RatingRequest request) {
        Video video = videoService.getByTitle(request.getTitle());
        if (video == null) {
            throw new IllegalArgumentException("Video not found");
        }

        Customer customer = customerService.getByUsername(request.getUsername());
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        Rating rating = new Rating(video, customer, request.getScore());
        return ratingService.addRating(rating);
    }

    @DeleteMapping("/{id}")
    public void deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
    }

    @GetMapping("/video/{videoId}")
    public List<Rating> findByVideoId(@PathVariable Long videoId) {
        return ratingService.findByVideoId(videoId);
    }

    @GetMapping("/customer/{customerId}")
    public List<Rating> findByCustomerId(@PathVariable Long customerId) {
        return ratingService.findByCustomerId(customerId);
    }

    @DeleteMapping("/video/all/{videoId}")
    public void deleteAllByVideoId(@PathVariable Long videoId) {
        ratingService.deleteAllByVideoId(videoId);
    }

    @DeleteMapping("/customer/all/{customerId}")
    public void deleteAllByCustomerId(@PathVariable Long customerId) {
        ratingService.deleteAllByCustomerId(customerId);
    }

    @GetMapping("/average/{id}")
    public Double getAverageScoreByVideoId(@PathVariable Long id) {
        return ratingService.getAverageScoreByVideoId(id);
    }
}

