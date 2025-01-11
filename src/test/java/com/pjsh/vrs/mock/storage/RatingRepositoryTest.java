package com.pjsh.vrs.mock.storage;

import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RatingRepository;
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

        rating = new Rating();
        rating.setVideo(video);
        rating.setCustomer(customer);
        rating.setScore(5);
        ratingRepository.save(rating);
    }

    @Test
    public void testFindByVideoId() {
        List<Rating> ratings = ratingRepository.findByVideoId(video.getId());

        assertThat(ratings).hasSize(1);
        assertThat(ratings.get(0).getScore()).isEqualTo(5);
    }

    @Test
    public void testFindByCustomerId() {
        List<Rating> ratings = ratingRepository.findByCustomerId(customer.getId());

        assertThat(ratings).hasSize(1);
        assertThat(ratings.get(0).getScore()).isEqualTo(5);
    }
}
