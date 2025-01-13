package com.pjsh.vrs.mock.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.RatingService;
import com.pjsh.vrs.storage.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @Autowired
    private Video video1, video2;

    @Autowired
    private Customer customer1, customer2;

    private Rating rating1, rating2;

    private Long video1Id, video2Id, customer1Id, customer2Id, rating1Id, rating2Id;

    @Value("${rating1.score}")
    private int rating1Score;
    @Value("${rating2.score}")
    private int rating2Score;

    @BeforeEach
    public void setUp() {
        video1Id = 1L;
        video2Id = 2L;
        customer1Id = 1L;
        customer2Id = 2L;
        rating1Id = 1L;
        rating2Id = 2L;

        rating1 = new Rating(video1, customer1, rating1Score);
        rating2 = new Rating(video1, customer2, rating2Score);
    }

    @Test
    public void testGetRatingsByVideoId() {
        when(ratingRepository.findByVideoId(video1Id)).thenReturn(List.of(rating1, rating2));

        List<Rating> ratings = ratingService.findByVideoId(video1Id);

        assertThat(ratings).isNotEmpty();
        assertThat(ratings.get(0).getScore()).isEqualTo(rating1Score);
        assertThat(ratings.get(1).getScore()).isEqualTo(rating2Score);
    }

    @Test
    public void testGetRatingsByCustomerId() {
        when(ratingRepository.findByCustomerId(customer1Id)).thenReturn(List.of(rating1));

        List<Rating> ratings = ratingService.findByCustomerId(customer1Id);

        assertThat(ratings).isNotEmpty();
        assertThat(ratings.get(0).getScore()).isEqualTo(rating1Score);
    }

    @Test
    public void testAddRating() {
        when(ratingRepository.save(rating2)).thenReturn(rating2);

        Rating addedRating = ratingService.addRating(rating2);

        assertThat(addedRating).isNotNull();
        assertThat(addedRating.getScore()).isEqualTo(rating2Score);
    }

    @Test
    public void testDeleteRating() {
        doNothing().when(ratingRepository).deleteById(rating1Id);

        ratingService.deleteRating(rating1Id);

        verify(ratingRepository, times(1)).deleteById(rating1Id);
    }

    @Test
    public void testDeleteAllByVideoId() {
        doNothing().when(ratingRepository).deleteAllByVideoId(video1Id);

        ratingService.deleteAllByVideoId(video1Id);

        verify(ratingRepository, times(1)).deleteAllByVideoId(video1Id);
    }

    @Test
    public void testDeleteAllByCustomerId() {
        doNothing().when(ratingRepository).deleteAllByCustomerId(customer1Id);

        ratingService.deleteAllByCustomerId(customer1Id);

        verify(ratingRepository, times(1)).deleteAllByCustomerId(customer1Id);
    }

    @Test
    public void testGetAverageScoreByVideoId() {
        double averageScore = (double) (rating1Score + rating2Score) / 2;
        when(ratingRepository.calculateAverageScoreByVideoId(video1Id)).thenReturn(averageScore);

        Double result = ratingService.getAverageScoreByVideoId(video1Id);

        assertThat(result).isEqualTo(averageScore);
    }
}
