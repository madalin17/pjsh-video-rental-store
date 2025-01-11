package com.pjsh.vrs.mock.storage;

import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.ReviewRepository;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Review review;
    private Video video;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        video = new Video();
        video.setTitle("Inception");
        video.setDirector("Christopher Nolan");
        video.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt");
        video.setYear(2010);
        video.setDuration("148 min");
        video.setGenre("Sci-Fi");
        video.setDescription("A mind-bending thriller about dreams within dreams.");
        video.setQuantity(5);
        videoRepository.save(video);

        customer = new Customer();
        customer.setUsername("john_doe");
        customer.setFullName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password123");
        customerRepository.save(customer);

        review = new Review();
        review.setVideo(video);
        review.setCustomer(customer);
        review.setDescription("Great video!");
        reviewRepository.save(review);
    }

    @Test
    public void testFindByVideoId() {
        List<Review> reviews = reviewRepository.findByVideoId(video.getId());

        assertThat(reviews).hasSize(1);
        assertThat(reviews.get(0).getDescription()).isEqualTo("Great video!");
    }

    @Test
    public void testFindByCustomerId() {
        List<Review> reviews = reviewRepository.findByCustomerId(customer.getId());

        assertThat(reviews).hasSize(1);
        assertThat(reviews.get(0).getDescription()).isEqualTo("Great video!");
    }
}
