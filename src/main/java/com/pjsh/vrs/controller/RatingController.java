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

    @GetMapping("/video/{videoId}")
    public List<Rating> getRatingsByVideoId(@PathVariable Long videoId) {
        return ratingService.getRatingsByVideoId(videoId);
    }

    @GetMapping("/customer/{customerId}")
    public List<Rating> getRatingsByCustomerId(@PathVariable Long customerId) {
        return ratingService.getRatingsByCustomerId(customerId);
    }

    @PostMapping
    public Rating addRating(@RequestBody Rating rating) {
        return ratingService.addRating(rating);
    }

    @DeleteMapping("/{id}")
    public void deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
    }
}

