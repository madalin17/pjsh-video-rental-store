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

    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating getRating(Long id) {
        return ratingRepository.getReferenceById(id);
    }

    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }

    public List<Rating> getRatingsByVideoId(Long videoId) {
        return ratingRepository.findByVideoId(videoId);
    }

    public List<Rating> getRatingsByCustomerId(Long customerId) {
        return ratingRepository.findByCustomerId(customerId);
    }

    public void findAllByVideoId(Long videoId) {
        ratingRepository.findAllByVideoId(videoId);
    }

    public void findAllByCustomerId(Long customerId) {
        ratingRepository.findAllByCustomerId(customerId);
    }

    public void deleteAllByVideoId(Long videoId) {
        ratingRepository.deleteAllByVideoId(videoId);
    }

    public void deleteAllByCustomerId(Long customerId) {
        ratingRepository.deleteAllByCustomerId(customerId);
    }

    public Double getAverageScoreByVideoId(Long videoId) {
        Double averageScore = ratingRepository.calculateAverageScoreByVideoId(videoId);
        return averageScore != null ? averageScore : 0.0;
    }
}
