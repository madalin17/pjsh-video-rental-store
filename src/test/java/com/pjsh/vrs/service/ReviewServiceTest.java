package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.storage.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;

    @BeforeEach
    public void setUp() {
        review = new Review();
        review.setDescription("Great video!");
    }

    @Test
    public void testGetReviewsByVideoId() {
        // Mock repository call
        when(reviewRepository.findByVideoId(1L)).thenReturn(List.of(review));

        // Call service method
        List<Review> reviews = reviewService.getReviewsByVideoId(1L);

        // Verify result
        assertThat(reviews).isNotEmpty();
        assertThat(reviews.get(0).getDescription()).isEqualTo("Great video!");
    }

    @Test
    public void testGetReviewsByCustomerId() {
        // Mock repository call
        when(reviewRepository.findByCustomerId(1L)).thenReturn(List.of(review));

        // Call service method
        List<Review> reviews = reviewService.getReviewsByCustomerId(1L);

        // Verify result
        assertThat(reviews).isNotEmpty();
        assertThat(reviews.get(0).getDescription()).isEqualTo("Great video!");
    }

    @Test
    public void testAddReview() {
        // Mock repository call
        when(reviewRepository.save(review)).thenReturn(review);

        // Call service method
        Review addedReview = reviewService.addReview(review);

        // Verify result
        assertThat(addedReview).isNotNull();
        assertThat(addedReview.getDescription()).isEqualTo("Great video!");
    }

    @Test
    public void testDeleteReview() {
        // Call service method
        reviewService.deleteReview(1L);

        // Verify that the repository method was called
        verify(reviewRepository, times(1)).deleteById(1L);
    }
}
