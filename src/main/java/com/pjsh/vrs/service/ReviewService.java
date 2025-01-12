package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.storage.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review getReview(Long id) {
        return reviewRepository.getReferenceById(id);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<Review> findByVideoId(Long videoId) {
        return reviewRepository.findByVideoId(videoId);
    }

    public List<Review> findByCustomerId(Long customerId) {
        return reviewRepository.findByCustomerId(customerId);
    }

    public void deleteAllByVideoId(Long videoId) {
        reviewRepository.deleteAllByVideoId(videoId);
    }

    public void deleteAllByCustomerId(Long customerId) {
        reviewRepository.deleteAllByCustomerId(customerId);
    }
}

