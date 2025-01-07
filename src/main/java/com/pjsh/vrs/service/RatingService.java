package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.storage.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> getRatingsByVideoId(Long videoId) {
        return ratingRepository.findByVideoId(videoId);
    }

    public List<Rating> getRatingsByCustomerId(Long customerId) {
        return ratingRepository.findByCustomerId(customerId);
    }

    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }
}
