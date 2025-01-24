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
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

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

    private Review testReview1, testReview2, testReview3;

    @Value("${review1.description}")
    private String review1Description;
    @Value("${review2.description}")
    private String review2Description;
    @Value("${review3.description}")
    private String review3Description;
    @Value("${review4.description}")
    private String review4Description;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));
        testVideo2 = videoRepository.save(new Video(video2));

        testCustomer1 = customerRepository.save(new Customer(customer1));
        testCustomer2 = customerRepository.save(new Customer(customer2));

        testReview1 = reviewRepository.save(new Review(testVideo1, testCustomer1, review1Description));
        testReview2 = reviewRepository.save(new Review(testVideo1, testCustomer2, review2Description));
        testReview3 = reviewRepository.save(new Review(testVideo2, testCustomer1, review3Description));
    }

    @AfterAll
    void cleanUp() {
        reviewRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void testGetReviewsByVideoId() {
        List<Review> reviews = reviewService.findByVideoId(testVideo1.getId());
        assertThat(reviews).hasSize(2);
        assertThat(reviews.get(0).getDescription()).isEqualTo(review1Description);
        assertThat(reviews.get(1).getDescription()).isEqualTo(review2Description);
    }

    @Test
    void testGetReviewsByCustomerId() {
        List<Review> reviews = reviewService.findByCustomerId(testCustomer1.getId());
        assertThat(reviews).hasSize(2);
        assertThat(reviews.get(0).getDescription()).isEqualTo(review1Description);
        assertThat(reviews.get(1).getDescription()).isEqualTo(review3Description);
    }

    @Test
    void testAddReview() {
        Review testReview4 = new Review();
        testReview4.setCustomer(testCustomer2);
        testReview4.setVideo(testVideo2);
        testReview4.setDescription(review4Description);

        Review savedReview = reviewService.addReview(testReview4);

        assertNotNull(savedReview.getId());
        assertThat(savedReview.getDescription()).isEqualTo(review4Description);

        List<Review> reviews = reviewService.findByVideoId(testVideo2.getId());
        assertThat(reviews).hasSize(2);
    }

    @Test
    void testUpdateReview() {
        testReview1.setDescription("Updated review: Fantastic movie!");
        Review updatedReview = reviewService.addReview(testReview1);

        assertThat(updatedReview.getDescription()).isEqualTo("Updated review: Fantastic movie!");

        Review fetchedReview = reviewService.getReview(testReview1.getId());
        assertThat(fetchedReview.getDescription()).isEqualTo("Updated review: Fantastic movie!");
    }

    @Test
    void testDeleteReviewById() {
        reviewService.deleteReview(testReview1.getId());

        List<Review> reviews = reviewService.findByVideoId(testVideo1.getId());
        assertThat(reviews).hasSize(1);
    }

    @Test
    void testDeleteAllReviewsByVideoId() {
        reviewService.deleteAllByVideoId(testVideo1.getId());

        List<Review> reviews = reviewService.findByVideoId(testVideo1.getId());
        assertThat(reviews).isEmpty();
    }

    @Test
    void testDeleteAllReviewsByCustomerId() {
        reviewService.deleteAllByCustomerId(testCustomer1.getId());

        List<Review> reviews = reviewService.findByCustomerId(testCustomer1.getId());
        assertThat(reviews).isEmpty();

        List<Review> reviewsByVideo = reviewService.findByVideoId(testVideo1.getId());
        assertThat(reviewsByVideo).hasSize(1);
    }

    @Test
    void testGetReviewById() {
        Review fetchedReview = reviewService.getReview(testReview1.getId());
        assertThat(fetchedReview).isNotNull();
        assertThat(fetchedReview.getDescription()).isEqualTo(review1Description);
    }
}

