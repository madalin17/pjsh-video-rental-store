package com.pjsh.vrs.integration.storage;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RatingRepository;
import com.pjsh.vrs.storage.VideoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RatingRepositoryIntegrationTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Video video1, video2;
    private Customer customer1, customer2;

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

        Rating rating1 = new Rating();
        rating1.setVideo(video1);
        rating1.setCustomer(customer1);
        rating1.setScore(4);
        ratingRepository.save(rating1);

        Rating rating2 = new Rating();
        rating2.setVideo(video1);
        rating2.setCustomer(customer2);
        rating2.setScore(5);
        ratingRepository.save(rating2);

        Rating rating3 = new Rating();
        rating3.setVideo(video2);
        rating3.setCustomer(customer1);
        rating3.setScore(3);
        ratingRepository.save(rating3);
    }

    @AfterAll
    public void cleanUp() {
        ratingRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testFindByVideoId() {
        List<Rating> ratings = ratingRepository.findByVideoId(video1.getId());

        assertNotNull(ratings);
        assertEquals(2, ratings.size());
        assertTrue(ratings.stream().anyMatch(r -> r.getScore() == 4));
        assertTrue(ratings.stream().anyMatch(r -> r.getScore() == 5));
    }

    @Test
    public void testFindByCustomerId() {
        List<Rating> ratings = ratingRepository.findByCustomerId(customer1.getId());

        assertNotNull(ratings);
        assertEquals(2, ratings.size());
        assertTrue(ratings.stream().anyMatch(r -> r.getVideo().equals(video1)));
        assertTrue(ratings.stream().anyMatch(r -> r.getVideo().equals(video2)));
    }

    @Test
    public void testDeleteAllByVideoId() {
        ratingRepository.deleteAllByVideoId(video1.getId());

        List<Rating> ratings = ratingRepository.findByVideoId(video1.getId());
        assertTrue(ratings.isEmpty());
    }

    @Test
    public void testDeleteAllByCustomerId() {
        ratingRepository.deleteAllByCustomerId(customer1.getId());

        List<Rating> ratings = ratingRepository.findByCustomerId(customer1.getId());
        assertTrue(ratings.isEmpty());
    }

    @Test
    public void testCalculateAverageScoreByVideoId() {
        Double averageScore = ratingRepository.calculateAverageScoreByVideoId(video1.getId());

        assertNotNull(averageScore);
        assertEquals(4.5, averageScore);
    }
}
