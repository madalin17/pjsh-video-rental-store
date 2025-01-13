package com.pjsh.vrs.integration.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RatingRepository;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RatingServiceTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Video video1;

    @Autowired
    private Customer customer1, customer2;

    private Video testVideo1;

    private Customer testCustomer1, testCustomer2;

    private Rating testRating1, testRating2, testRating3;

    @Value("${rating1.score}")
    private Integer rating1Score;
    @Value("${rating2.score}")
    private Integer rating2Score;
    @Value("${rating3.score}")
    private Integer rating3Score;
    @Value("${video1.title}")
    private String video1Title;
    @Value("${customer2.email}")
    private String customer2Email;

    @BeforeEach
    public void setUp() {
        ratingRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));

        testCustomer1 = customerRepository.save(new Customer(customer1));
        testCustomer2 = customerRepository.save(new Customer(customer2));

        testRating1 = ratingRepository.save(new Rating(testVideo1, testCustomer1, rating1Score));
        testRating2 = ratingRepository.save(new Rating(testVideo1, testCustomer2, rating2Score));
    }

    @AfterAll
    public void cleanUp() {
        ratingRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testAddMultipleRatings() {
        List<Rating> ratings = ratingRepository.findByVideoId(testVideo1.getId());

        assertThat(ratings).hasSize(2);
        assertThat(ratings.get(0).getScore()).isEqualTo(rating1Score);
        assertThat(ratings.get(1).getScore()).isEqualTo(rating2Score);
    }

    @Test
    public void testUpdateRating() {
        testRating2 = ratingRepository.save(new Rating(testVideo1, testCustomer2, rating3Score));

        Rating updatedRating = ratingRepository.save(testRating2);

        assertThat(updatedRating.getVideo().getTitle()).isEqualTo(video1Title);
        assertThat(updatedRating.getCustomer().getEmail()).isEqualTo(customer2Email);
        assertThat(updatedRating.getScore()).isEqualTo(rating3Score);
    }

    @Test
    public void testFindRatingsByCustomerId() {
        List<Rating> ratings = ratingRepository.findByCustomerId(testCustomer1.getId());

        assertThat(ratings).hasSize(1);
        assertThat(ratings.get(0).getScore()).isEqualTo(rating1Score);
    }

    @Test
    public void testFindRatingsByInvalidVideoId() {
        List<Rating> ratings = ratingRepository.findByVideoId(999L);

        assertThat(ratings).isEmpty();
    }

    @Test
    public void testDeleteAllRatingsForVideo() {
        ratingRepository.deleteAllByVideoId(video1.getId());

        List<Rating> ratings = ratingRepository.findByVideoId(video1.getId());
        assertThat(ratings).isEmpty();
    }

    @Test
    public void testDeleteAllRatingsForCustomer() {
        ratingRepository.deleteAllByCustomerId(customer2.getId());

        List<Rating> ratings = ratingRepository.findByCustomerId(customer2.getId());
        assertThat(ratings).isEmpty();
    }

    @Test
    public void testFindRatingById() {
        Optional<Rating> foundRating = ratingRepository.findById(testRating1.getId());

        assertThat(foundRating).isPresent();
        assertThat(foundRating.get().getScore()).isEqualTo(rating1Score);
    }

    @Test
    public void testFindAverageScoreForVideo() {
        Double averageScore = ratingRepository.calculateAverageScoreByVideoId(testVideo1.getId());

        assertThat(averageScore).isEqualTo((double) (rating1Score + rating2Score) / 2);
    }
}
