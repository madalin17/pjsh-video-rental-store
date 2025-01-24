package com.pjsh.vrs.integration.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.AuditService;
import com.pjsh.vrs.service.RatingService;
import com.pjsh.vrs.service.RentalService;
import com.pjsh.vrs.service.ReviewService;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RatingRepository;
import com.pjsh.vrs.storage.ReviewRepository;
import com.pjsh.vrs.storage.RentalRepository;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class AuditServiceTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private Video video1;

    @Autowired
    private Customer customer1;

    private Video testVideo1;

    private Customer testCustomer1;

    private ByteArrayOutputStream outputStreamCaptor;

    @Value("${rating1.score}")
    private Integer rating1Score;
    @Value("${review1.description}")
    private String review1Description;

    @BeforeEach
    public void setUp() {
        rentalRepository.deleteAll();
        ratingRepository.deleteAll();
        reviewRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        testVideo1 = videoRepository.save(new Video(video1));

        testCustomer1 = customerRepository.save(new Customer(customer1));
    }

    @AfterAll
    public void cleanUp() {
        System.setOut(System.out);
        rentalRepository.deleteAll();
        ratingRepository.deleteAll();
        reviewRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testCreateRatingAudits() throws InterruptedException {
        System.setOut(new PrintStream(outputStreamCaptor));

        Rating rating = new Rating(testVideo1, testCustomer1, rating1Score);
        ratingService.addRating(rating);

        String capturedOutput = outputStreamCaptor.toString();
        assertTrue(capturedOutput.contains("Rating Event: Customer " + testCustomer1.getId() + " rated video " + testVideo1.getId() + " - Rating: " + rating1Score));
    }

    @Test
    public void testCreateReviewAudits() throws InterruptedException {
        System.setOut(new PrintStream(outputStreamCaptor));

        Review review = new Review(testVideo1, testCustomer1, review1Description);
        reviewService.addReview(review);

        String capturedOutput = outputStreamCaptor.toString();
        assertTrue(capturedOutput.contains("Review Event: Customer " + testCustomer1.getId() + " reviewed video " + testVideo1.getId() + " - Review: " + review1Description));
    }

    @Test
    public void testReturnVideoAudits() throws InterruptedException {
        Rental rental = rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId());

        rentalService.returnVideo(rental.getId());

        String capturedOutput = outputStreamCaptor.toString();
        assertTrue(capturedOutput.contains("Rental Event: Rental " + rental.getId() + " - Event Type: RETURN"));
    }
}

