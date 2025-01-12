package com.pjsh.vrs.integration.storage;

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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RatingRepositoryIntegrationTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Video video1, video2;

    @Autowired
    private Customer customer1, customer2;

    private Video testVideo1, testVideo2;

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
        testVideo2 = videoRepository.save(new Video(video2));

        testCustomer1 = customerRepository.save(new Customer(customer1));
        testCustomer2 = customerRepository.save(new Customer(customer2));

        testRating1 = ratingRepository.save(new Rating(testVideo1, testCustomer1, rating1Score));
        testRating2 = ratingRepository.save(new Rating(testVideo1, testCustomer2, rating2Score));
        testRating2 = ratingRepository.save(new Rating(testVideo2, testCustomer1, rating3Score));
    }

    @AfterAll
    public void cleanUp() {
        ratingRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testFindByVideoId() {
        List<Rating> ratings = ratingRepository.findByVideoId(testVideo1.getId());

        assertNotNull(ratings);
        assertEquals(2, ratings.size());
        assertTrue(ratings.stream().anyMatch(r -> r.getScore() == rating1Score));
        assertTrue(ratings.stream().anyMatch(r -> r.getScore() == rating2Score));
    }

    @Test
    public void testFindByCustomerId() {
        List<Rating> ratings = ratingRepository.findByCustomerId(testCustomer1.getId());

        assertNotNull(ratings);
        assertEquals(2, ratings.size());
        assertTrue(ratings.stream().anyMatch(r -> r.getVideo().equals(testVideo1)));
        assertTrue(ratings.stream().anyMatch(r -> r.getVideo().equals(testVideo2)));
    }

    @Test
    public void testDeleteAllByVideoId() {
        ratingRepository.deleteAllByVideoId(testVideo1.getId());

        List<Rating> ratings = ratingRepository.findByVideoId(testVideo1.getId());
        assertTrue(ratings.isEmpty());
    }

    @Test
    public void testDeleteAllByCustomerId() {
        ratingRepository.deleteAllByCustomerId(testCustomer1.getId());

        List<Rating> ratings = ratingRepository.findByCustomerId(testCustomer1.getId());
        assertTrue(ratings.isEmpty());
    }

    @Test
    public void testCalculateAverageScoreByVideoId() {
        Double averageScore = ratingRepository.calculateAverageScoreByVideoId(testVideo1.getId());

        assertNotNull(averageScore);
        assertEquals((double) (rating1Score + rating2Score) / 2, averageScore);
    }
}
