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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatingServiceIntegrationTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;
    private Video video1;
    private Video video2;

    @BeforeEach
    public void setUp() {
        ratingRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        video1 = new Video();
        video1.setTitle("Inception");
        video1.setDirector("Christopher Nolan");
        video1.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt");
        video1.setYear(2010);
        video1.setDuration("148 min");
        video1.setGenre("Sci-Fi");
        video1.setDescription("A mind-bending thriller about dreams within dreams.");
        video1.setQuantity(5);
        videoRepository.save(video1);

        video2 = new Video();
        video2.setTitle("Titanic");
        video2.setDirector("James Cameron");
        video2.setActors("Leonardo DiCaprio, Kate Winslet");
        video2.setYear(1997);
        video2.setDuration("195 min");
        video2.setGenre("Romance");
        video2.setDescription("A tragic love story set against the backdrop of the Titanic.");
        video2.setQuantity(3);
        videoRepository.save(video2);

        customer1 = new Customer();
        customer1.setUsername("john_doe");
        customer1.setFullName("John Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setPassword("password123");
        customerRepository.save(customer1);

        customer2 = new Customer();
        customer2.setUsername("jane_doe");
        customer2.setFullName("Jane Doe");
        customer2.setEmail("jane.doe@example.com");
        customer2.setPassword("password456");
        customerRepository.save(customer2);
    }

    @AfterAll
    public void cleanUp() {
        ratingRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testAddMultipleRatings() {
        Rating rating1 = new Rating();
        rating1.setScore(5);
        rating1.setCustomer(customer1);
        rating1.setVideo(video1);
        ratingRepository.save(rating1);

        Rating rating2 = new Rating();
        rating2.setScore(3);
        rating2.setCustomer(customer2);
        rating2.setVideo(video1);
        ratingRepository.save(rating2);

        List<Rating> ratings = ratingRepository.findByVideoId(video1.getId());
        assertThat(ratings).hasSize(2);
        assertThat(ratings.get(0).getScore()).isEqualTo(5);
        assertThat(ratings.get(1).getScore()).isEqualTo(3);
    }

    @Test
    public void testUpdateRating() {
        Rating rating = new Rating();
        rating.setScore(4);
        rating.setCustomer(customer1);
        rating.setVideo(video2);
        Rating savedRating = ratingRepository.save(rating);

        savedRating.setScore(5);
        Rating updatedRating = ratingRepository.save(savedRating);

        assertThat(updatedRating.getScore()).isEqualTo(5);
        assertThat(updatedRating.getCustomer().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(updatedRating.getVideo().getTitle()).isEqualTo("Titanic");
    }

    @Test
    public void testFindRatingsByCustomerId() {
        Rating rating1 = new Rating();
        rating1.setScore(5);
        rating1.setCustomer(customer1);
        rating1.setVideo(video1);
        ratingRepository.save(rating1);

        Rating rating2 = new Rating();
        rating2.setScore(4);
        rating2.setCustomer(customer1);
        rating2.setVideo(video2);
        ratingRepository.save(rating2);

        List<Rating> ratings = ratingRepository.findByCustomerId(customer1.getId());

        assertThat(ratings).hasSize(2);
        assertThat(ratings.get(0).getScore()).isEqualTo(5);
        assertThat(ratings.get(1).getScore()).isEqualTo(4);
    }

    @Test
    public void testFindRatingsByInvalidVideoId() {
        List<Rating> ratings = ratingRepository.findByVideoId(999L);

        assertThat(ratings).isEmpty();
    }

    @Test
    public void testDeleteAllRatingsForVideo() {
        Rating rating1 = new Rating();
        rating1.setScore(5);
        rating1.setCustomer(customer1);
        rating1.setVideo(video1);
        ratingRepository.save(rating1);

        Rating rating2 = new Rating();
        rating2.setScore(3);
        rating2.setCustomer(customer2);
        rating2.setVideo(video1);
        ratingRepository.save(rating2);

        ratingRepository.deleteAllByVideoId(video1.getId());

        List<Rating> ratings = ratingRepository.findByVideoId(video1.getId());
        assertThat(ratings).isEmpty();
    }

    @Test
    public void testDeleteAllRatingsForCustomer() {
        Rating rating1 = new Rating();
        rating1.setScore(4);
        rating1.setCustomer(customer2);
        rating1.setVideo(video2);
        ratingRepository.save(rating1);

        Rating rating2 = new Rating();
        rating2.setScore(5);
        rating2.setCustomer(customer2);
        rating2.setVideo(video1);
        ratingRepository.save(rating2);

        ratingRepository.deleteAllByCustomerId(customer2.getId());

        List<Rating> ratings = ratingRepository.findByCustomerId(customer2.getId());
        assertThat(ratings).isEmpty();
    }

    @Test
    public void testFindRatingById() {
        Rating rating = new Rating();
        rating.setScore(3);
        rating.setCustomer(customer1);
        rating.setVideo(video1);
        Rating savedRating = ratingRepository.save(rating);

        Optional<Rating> foundRating = ratingRepository.findById(savedRating.getId());
        assertThat(foundRating).isPresent();
        assertThat(foundRating.get().getScore()).isEqualTo(3);
    }

    @Test
    public void testFindAverageScoreForVideo() {
        Rating rating1 = new Rating();
        rating1.setScore(5);
        rating1.setCustomer(customer1);
        rating1.setVideo(video1);
        ratingRepository.save(rating1);

        Rating rating2 = new Rating();
        rating2.setScore(3);
        rating2.setCustomer(customer2);
        rating2.setVideo(video1);
        ratingRepository.save(rating2);

        Double averageScore = ratingRepository.calculateAverageScoreByVideoId(video1.getId());

        assertThat(averageScore).isEqualTo(4.0);
    }
}
