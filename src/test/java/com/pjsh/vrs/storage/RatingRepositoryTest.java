package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Rating rating;
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

        rating = new Rating();
        rating.setVideo(video);
        rating.setCustomer(customer);
        rating.setScore(5);
        ratingRepository.save(rating);
    }

    @Test
    public void testFindByVideoId() {
        // Retrieve rating by video ID
        List<Rating> ratings = ratingRepository.findByVideoId(video.getId());

        // Verify result
        assertThat(ratings).hasSize(1);
        assertThat(ratings.get(0).getScore()).isEqualTo(5);
    }

    @Test
    public void testFindByCustomerId() {
        // Retrieve rating by customer ID
        List<Rating> ratings = ratingRepository.findByCustomerId(customer.getId());

        // Verify result
        assertThat(ratings).hasSize(1);
        assertThat(ratings.get(0).getScore()).isEqualTo(5);
    }
}
