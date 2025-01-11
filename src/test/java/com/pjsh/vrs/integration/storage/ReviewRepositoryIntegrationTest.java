package com.pjsh.vrs.integration.storage;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.ReviewRepository;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewRepositoryIntegrationTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Video video1, video2;
    private Customer customer1, customer2;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
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

        Review review1 = new Review();
        review1.setCustomer(customer1);
        review1.setVideo(video1);
        review1.setDescription("Great movie!");
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setCustomer(customer1);
        review2.setVideo(video2);
        review2.setDescription("Mind-blowing visuals.");
        reviewRepository.save(review2);

        Review review3 = new Review();
        review3.setCustomer(customer1);
        review3.setVideo(video1);
        review3.setDescription("Damn.");
        reviewRepository.save(review3);
    }

    @Test
    void testFindByVideoId() {
        List<Review> reviews = reviewRepository.findByVideoId(video1.getId());
        assertThat(reviews).hasSize(1);
        assertThat(reviews.get(0).getDescription()).isEqualTo("Great movie!");
    }

    @Test
    void testFindByCustomerId() {
        List<Review> reviews = reviewRepository.findByCustomerId(customer1.getId());
        assertThat(reviews).hasSize(2);
        assertThat(reviews.get(0).getCustomer().getFullName()).isEqualTo("John Doe");
    }

    @Test
    void testFindAllByVideoId() {
        List<Review> reviews = reviewRepository.findAllByVideoId(video1.getId());
        assertThat(reviews).hasSize(2);
        assertThat(reviews.get(0).getVideo().getTitle()).isEqualTo("Inception");
    }

    @Test
    void testFindAllByCustomerId() {
        List<Review> reviews = reviewRepository.findAllByCustomerId(customer1.getId());
        assertThat(reviews).hasSize(2);
        assertThat(reviews.get(0).getDescription()).isEqualTo("Great movie!");
        assertThat(reviews.get(1).getDescription()).isEqualTo("Damn.");
    }

    @Test
    void testDeleteAllByVideoId() {
        reviewRepository.deleteAllByVideoId(video1.getId());
        List<Review> reviews = reviewRepository.findByVideoId(video1.getId());
        assertThat(reviews).isEmpty();
    }

    @Test
    void testDeleteAllByCustomerId() {
        reviewRepository.deleteAllByCustomerId(customer1.getId());
        List<Review> reviews = reviewRepository.findByCustomerId(customer1.getId());
        assertThat(reviews).isEmpty();
    }
}

