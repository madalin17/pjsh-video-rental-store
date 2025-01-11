package com.pjsh.vrs.integration.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.ReviewService;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.ReviewRepository;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewServiceIntegrationTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Video video1, video2;
    private Customer customer1, customer2;
    private Review review1, review2, review3;

    @BeforeEach
    public void setUp() {
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

        review1 = new Review();
        review1.setCustomer(customer1);
        review1.setVideo(video1);
        review1.setDescription("Amazing movie!");

        review2 = new Review();
        review2.setCustomer(customer1);
        review2.setVideo(video2);
        review2.setDescription("Not bad, but could be better.");

        review3 = new Review();
        review3.setCustomer(customer2);
        review3.setVideo(video1);
        review3.setDescription("A masterpiece!");

        reviewRepository.saveAll(List.of(review1, review2, review3));
    }

    @AfterAll
    public void cleanUp() {
        reviewRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testGetReviewsByVideoId() {
        List<Review> reviews = reviewService.getReviewsByVideoId(video1.getId());
        assertThat(reviews).hasSize(2);
        assertThat(reviews.get(0).getDescription()).isEqualTo("Amazing movie!");
        assertThat(reviews.get(1).getDescription()).isEqualTo("A masterpiece!");
    }

    @Test
    public void testGetReviewsByCustomerId() {
        List<Review> reviews = reviewService.getReviewsByCustomerId(customer1.getId());
        assertThat(reviews).hasSize(2);
        assertThat(reviews.get(0).getDescription()).isEqualTo("Amazing movie!");
        assertThat(reviews.get(1).getDescription()).isEqualTo("Not bad, but could be better.");
    }

    @Test
    public void testAddReview() {
        Review newReview = new Review();
        newReview.setCustomer(customer2);
        newReview.setVideo(video2);
        newReview.setDescription("Loved it!");

        Review savedReview = reviewService.addReview(newReview);

        assertThat(savedReview.getId()).isNotNull();
        assertThat(savedReview.getDescription()).isEqualTo("Loved it!");

        List<Review> reviews = reviewService.getReviewsByVideoId(video2.getId());
        assertThat(reviews).hasSize(2);
    }

    @Test
    public void testUpdateReview() {
        review1.setDescription("Updated review: Fantastic movie!");
        Review updatedReview = reviewService.addReview(review1);

        assertThat(updatedReview.getDescription()).isEqualTo("Updated review: Fantastic movie!");

        Review fetchedReview = reviewService.getReview(review1.getId());
        assertThat(fetchedReview.getDescription()).isEqualTo("Updated review: Fantastic movie!");
    }

    @Test
    public void testDeleteReviewById() {
        reviewService.deleteReview(review1.getId());

        List<Review> reviews = reviewService.getReviewsByVideoId(video1.getId());
        assertThat(reviews).hasSize(1); // Only review3 remains
    }

    @Test
    public void testDeleteAllReviewsByVideoId() {
        reviewService.deleteAllReviewsByVideoId(video1.getId());

        List<Review> reviews = reviewService.getReviewsByVideoId(video1.getId());
        assertThat(reviews).isEmpty();
    }

    @Test
    public void testDeleteAllReviewsByCustomerId() {
        reviewService.deleteAllReviewsByCustomerId(customer1.getId());

        List<Review> reviews = reviewService.getReviewsByCustomerId(customer1.getId());
        assertThat(reviews).isEmpty();

        List<Review> reviewsByVideo = reviewService.getReviewsByVideoId(video1.getId());
        assertThat(reviewsByVideo).hasSize(1);
    }

    @Test
    public void testGetReviewById() {
        Review fetchedReview = reviewService.getReview(review1.getId());
        assertThat(fetchedReview).isNotNull();
        assertThat(fetchedReview.getDescription()).isEqualTo("Amazing movie!");
    }
}

