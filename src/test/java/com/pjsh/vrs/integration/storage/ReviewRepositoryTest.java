package com.pjsh.vrs.integration.storage;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.ReviewRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class ReviewRepositoryTest {

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
    @Value("${video1.title}")
    private String video1Title;
    @Value("${customer2.fullName}")
    private String customer2FullName;

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
    public void cleanUp() {
        reviewRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void testFindByVideoId() {
        List<Review> reviews = reviewRepository.findByVideoId(testVideo1.getId());
        assertThat(reviews).hasSize(2);
        assertThat(reviews.get(0).getDescription()).isEqualTo(review1Description);
        assertThat(reviews.get(1).getDescription()).isEqualTo(review2Description);
    }

    @Test
    void testFindByCustomerId() {
        List<Review> reviews = reviewRepository.findByCustomerId(testCustomer2.getId());
        assertThat(reviews).hasSize(1);
        assertThat(reviews.get(0).getCustomer().getFullName()).isEqualTo(customer2FullName);
    }

    @Test
    void testDeleteAllByVideoId() {
        reviewRepository.deleteAllByVideoId(testVideo1.getId());
        List<Review> reviews = reviewRepository.findByVideoId(testVideo1.getId());
        assertThat(reviews).isEmpty();
    }

    @Test
    void testDeleteAllByCustomerId() {
        reviewRepository.deleteAllByCustomerId(testCustomer1.getId());
        List<Review> reviews = reviewRepository.findByCustomerId(testCustomer1.getId());
        assertThat(reviews).isEmpty();
    }
}

