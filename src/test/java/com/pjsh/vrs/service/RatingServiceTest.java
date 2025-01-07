package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.storage.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    private Rating rating;

    @BeforeEach
    public void setUp() {
        rating = new Rating();
        rating.setScore(5);
    }

    @Test
    public void testGetRatingsByVideoId() {
        // Mock repository call
        when(ratingRepository.findByVideoId(1L)).thenReturn(List.of(rating));

        // Call service method
        List<Rating> ratings = ratingService.getRatingsByVideoId(1L);

        // Verify result
        assertThat(ratings).isNotEmpty();
        assertThat(ratings.get(0).getScore()).isEqualTo(5);
    }

    @Test
    public void testGetRatingsByCustomerId() {
        // Mock repository call
        when(ratingRepository.findByCustomerId(1L)).thenReturn(List.of(rating));

        // Call service method
        List<Rating> ratings = ratingService.getRatingsByCustomerId(1L);

        // Verify result
        assertThat(ratings).isNotEmpty();
        assertThat(ratings.get(0).getScore()).isEqualTo(5);
    }

    @Test
    public void testAddRating() {
        // Mock repository call
        when(ratingRepository.save(rating)).thenReturn(rating);

        // Call service method
        Rating addedRating = ratingService.addRating(rating);

        // Verify result
        assertThat(addedRating).isNotNull();
        assertThat(addedRating.getScore()).isEqualTo(5);
    }

    @Test
    public void testDeleteRating() {
        // Call service method
        ratingService.deleteRating(1L);

        // Verify that the repository method was called
        verify(ratingRepository, times(1)).deleteById(1L);
    }
}
