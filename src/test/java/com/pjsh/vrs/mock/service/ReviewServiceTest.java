package com.pjsh.vrs.mock.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.ReviewService;
import com.pjsh.vrs.storage.ReviewRepository;
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
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Autowired
    private Video video1, video2;

    @Autowired
    private Customer customer1, customer2;

    private Review review1, review2;

    private Long video1Id, video2Id, customer1Id, customer2Id, review1Id, review2Id;

    @Value("${review1.description}")
    private String review1Description;
    @Value("${review2.description}")
    private String review2Description;

    @BeforeEach
    public void setUp() {
        video1Id = 1L;
        video2Id = 2L;
        customer1Id = 1L;
        customer2Id = 2L;
        review1Id = 1L;
        review2Id = 2L;

        review1 = new Review(video1, customer1, review1Description);
        review2 = new Review(video1, customer2, review2Description);
    }

    @Test
    public void testGetReviewsByVideoId() {
        when(reviewRepository.findByVideoId(video1Id)).thenReturn(List.of(review1, review2));

        List<Review> reviews = reviewService.findByVideoId(video1Id);

        assertThat(reviews).isNotEmpty();
        assertThat(reviews.get(0).getDescription()).isEqualTo(review1Description);
        assertThat(reviews.get(1).getDescription()).isEqualTo(review2Description);
    }

    @Test
    public void testGetReviewsByCustomerId() {
        when(reviewRepository.findByCustomerId(customer1Id)).thenReturn(List.of(review1));

        List<Review> reviews = reviewService.findByCustomerId(customer1Id);

        assertThat(reviews).isNotEmpty();
        assertThat(reviews.get(0).getDescription()).isEqualTo(review1Description);
    }

    @Test
    public void testAddReview() {
        when(reviewRepository.save(review2)).thenReturn(review2);

        Review addedReview = reviewService.addReview(review2);

        assertThat(addedReview).isNotNull();
        assertThat(addedReview.getDescription()).isEqualTo(review2Description);
    }

    @Test
    public void testDeleteReview() {
        doNothing().when(reviewRepository).deleteById(review1Id);

        reviewService.deleteReview(review1Id);

        verify(reviewRepository, times(1)).deleteById(review1Id);
    }

    @Test
    public void testDeleteAllByVideoId() {
        doNothing().when(reviewRepository).deleteAllByVideoId(video1Id);

        reviewService.deleteAllByVideoId(video1Id);

        verify(reviewRepository, times(1)).deleteAllByVideoId(video1Id);
    }

    @Test
    public void testDeleteAllByCustomerId() {
        doNothing().when(reviewRepository).deleteAllByCustomerId(customer1Id);

        reviewService.deleteAllByCustomerId(customer1Id);

        verify(reviewRepository, times(1)).deleteAllByCustomerId(customer1Id);
    }
}
