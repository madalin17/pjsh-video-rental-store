package com.pjsh.vrs.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.storage.ReviewRepository;
import com.pjsh.vrs.storage.VideoRepository;
import com.pjsh.vrs.storage.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Video video1, video2;

    @Autowired
    private Customer customer1, customer2;

    private Video testVideo1, testVideo2;

    private Customer testCustomer1, testCustomer2;

    private Review testReview1, testReview2;

    @Value("${review1.description}")
    private String review1Description;
    @Value("${review2.description}")
    private String review2Description;
    @Value("${review3.description}")
    private String review3Description;

    @BeforeEach
    public void setUp() {
        reviewRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));
        testVideo2 = videoRepository.save(new Video(video2));

        testCustomer1 = customerRepository.save(new Customer(customer1));
        testCustomer2 = customerRepository.save(new Customer(customer2));

        testReview1 = reviewRepository.save(new Review(testVideo1, testCustomer1, review1Description));
        testReview2 = reviewRepository.save(new Review(testVideo1, testCustomer2, review2Description));
    }

    @AfterEach
    public void cleanUp() {
        reviewRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testGetReviewsByVideoId() throws Exception {
        mockMvc.perform(get("/reviews/video/{videoId}", testVideo1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value(review1Description))
                .andExpect(jsonPath("$[1].description").value(review2Description));
    }

    @Test
    public void testGetReviewsByCustomerId() throws Exception {
        mockMvc.perform(get("/reviews/customer/{customerId}", testCustomer1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value(review1Description));
    }

    @Test
    public void testAddReview() throws Exception {
        Review review = new Review(testVideo2, testCustomer1, review3Description);

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(review3Description));
    }

    @Test
    public void testDeleteReview() throws Exception {
        mockMvc.perform(delete("/reviews/{id}", testReview1.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/reviews/video/{videoId}", testVideo1.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testDeleteAllReviewsByVideoId() throws Exception {
        mockMvc.perform(delete("/reviews/video/all/{videoId}", testVideo1.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/reviews/video/{videoId}", testVideo1.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testDeleteAllReviewsByCustomerId() throws Exception {
        mockMvc.perform(delete("/reviews/customer/all/{customerId}", testCustomer1.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/reviews/customer/{customerId}", testCustomer1.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
