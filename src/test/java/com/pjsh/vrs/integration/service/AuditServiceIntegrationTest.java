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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuditServiceIntegrationTest {

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
    private AuditService auditService; // Assuming AuditService is registered

    private Video video;
    private Customer customer;

    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    public void setUp() {
        rentalRepository.deleteAll();
        ratingRepository.deleteAll();
        reviewRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

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

        Rating rating = new Rating();
        rating.setCustomer(customer);
        rating.setVideo(video);
        rating.setScore(5);

        ratingService.addRating(rating);

        String capturedOutput = outputStreamCaptor.toString();
        assertTrue(capturedOutput.contains("Rating Event: Customer " + customer.getId() + " rated video " + video.getId() + " - Rating: " + rating.getScore()));
    }

    @Test
    public void testCreateReviewAudits() throws InterruptedException {
        System.setOut(new PrintStream(outputStreamCaptor));

        Review review = new Review();
        review.setCustomer(customer);
        review.setVideo(video);
        review.setDescription("Excellent movie!");

        reviewService.addReview(review);

        String capturedOutput = outputStreamCaptor.toString();
        assertTrue(capturedOutput.contains("Review Event: Customer " + customer.getId() + " reviewed video " + video.getId() + " - Review: " + review.getDescription()));
    }

    @Test
    public void testRentVideoAudits() throws InterruptedException {
        rentalService.rentVideo(customer.getId(), video.getId());

        String capturedOutput = outputStreamCaptor.toString();
        assertTrue(capturedOutput.contains("Rental Event: Customer " + customer.getId() + " rented video + " + video.getId() + " - Event Type: RENT"));
    }

    @Test
    public void testReturnVideoAudits() throws InterruptedException {
        Rental rental = rentalService.rentVideo(customer.getId(), video.getId());

        rentalService.returnVideo(rental.getId());

        String capturedOutput = outputStreamCaptor.toString();
        assertTrue(capturedOutput.contains("Rental Event: Rental " + rental.getId() + " - Event Type: RETURN"));
    }
}

