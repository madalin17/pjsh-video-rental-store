package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.storage.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    private Rating rating;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        rating = new Rating();
        rating.setScore(5);
    }

    @Test
    public void testGetRatingsByVideoId() {
        when(ratingRepository.findByVideoId(1L)).thenReturn(List.of(rating));

        List<Rating> ratings = ratingService.getRatingsByVideoId(1L);

        assertThat(ratings).isNotEmpty();
        assertThat(ratings.get(0).getScore()).isEqualTo(5);
    }

    @Test
    public void testGetRatingsByCustomerId() {
        when(ratingRepository.findByCustomerId(1L)).thenReturn(List.of(rating));

        List<Rating> ratings = ratingService.getRatingsByCustomerId(1L);

        assertThat(ratings).isNotEmpty();
        assertThat(ratings.get(0).getScore()).isEqualTo(5);
    }

    @Test
    public void testAddRating() {
        when(ratingRepository.save(rating)).thenReturn(rating);

        Rating addedRating = ratingService.addRating(rating);

        assertThat(addedRating).isNotNull();
        assertThat(addedRating.getScore()).isEqualTo(5);
    }

    @Test
    public void testDeleteRating() {
        ratingService.deleteRating(1L);

        verify(ratingRepository, times(1)).deleteById(1L);
    }
}
