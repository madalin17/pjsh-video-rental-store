package com.pjsh.vrs.controller;

import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public Rating addRating(@RequestBody Rating rating) {
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
    public void getAverageScoreByVideoId(@PathVariable Long id) {
        ratingService.getAverageScoreByVideoId(id);
    }
}

