package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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
        video.setYear(2010);
        video.setQuantity(10);
        videoRepository.save(video);

        customer = new Customer();
        customer.setUsername("john_doe");
        customer.setEmail("john.doe@example.com");
        customerRepository.save(customer);

        review = new Review();
        review.setVideo(video);
        review.setCustomer(customer);
        review.setDescription("Great video!");
        reviewRepository.save(review);
    }

    @Test
    public void testFindByVideoId() {
        // Retrieve review by video ID
        List<Review> reviews = reviewRepository.findByVideoId(video.getId());

        // Verify result
        assertThat(reviews).hasSize(1);
        assertThat(reviews.get(0).getDescription()).isEqualTo("Great video!");
    }

    @Test
    public void testFindByCustomerId() {
        // Retrieve review by customer ID
        List<Review> reviews = reviewRepository.findByCustomerId(customer.getId());

        // Verify result
        assertThat(reviews).hasSize(1);
        assertThat(reviews.get(0).getDescription()).isEqualTo("Great video!");
    }
}
