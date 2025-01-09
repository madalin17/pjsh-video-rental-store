package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.storage.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@ExtendWith(MockitoExtension.class)
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
        when(reviewRepository.findByVideoId(1L)).thenReturn(List.of(review));

        List<Review> reviews = reviewService.getReviewsByVideoId(1L);

        assertThat(reviews).isNotEmpty();
        assertThat(reviews.get(0).getDescription()).isEqualTo("Great video!");
    }

    @Test
    public void testGetReviewsByCustomerId() {
        when(reviewRepository.findByCustomerId(1L)).thenReturn(List.of(review));

        List<Review> reviews = reviewService.getReviewsByCustomerId(1L);

        assertThat(reviews).isNotEmpty();
        assertThat(reviews.get(0).getDescription()).isEqualTo("Great video!");
    }

    @Test
    public void testAddReview() {
        when(reviewRepository.save(review)).thenReturn(review);

        Review addedReview = reviewService.addReview(review);

        assertThat(addedReview).isNotNull();
        assertThat(addedReview.getDescription()).isEqualTo("Great video!");
    }

    @Test
    public void testDeleteReview() {
        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }
}
