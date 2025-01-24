package com.pjsh.vrs.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.controller.requests.RatingRequest;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RatingRepository;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.AfterAll;
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
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Video video1, video2;

    @Autowired
    private Customer customer1, customer2;

    private Video testVideo1, testVideo2;

    private Customer testCustomer1, testCustomer2;

    private Rating testRating1, testRating2;

    @Value("${rating1.score}")
    private int rating1Score;
    @Value("${rating2.score}")
    private int rating2Score;
    @Value("${rating3.score}")
    private int rating3Score;

    @BeforeEach
    public void setUp() {
        ratingRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));
        testVideo2 = videoRepository.save(new Video(video2));

        testCustomer1 = customerRepository.save(new Customer(customer1));
        testCustomer2 = customerRepository.save(new Customer(customer2));

        testRating1 = ratingRepository.save(new Rating(testVideo1, testCustomer1, rating1Score));
        testRating2 = ratingRepository.save(new Rating(testVideo1, testCustomer2, rating2Score));
    }

    @AfterAll
    public void cleanUp() {
        ratingRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testAddRating() throws Exception {
        RatingRequest request = new RatingRequest();
        request.setTitle(testVideo2.getTitle());
        request.setUsername(testCustomer1.getUsername());
        request.setScore(rating3Score);

        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(rating3Score));
    }

    @Test
    public void testDeleteRating() throws Exception {
        mockMvc.perform(delete("/ratings/{id}", testRating1.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/ratings/video/{videoId}", testVideo1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetRatingsByVideoId() throws Exception {
        mockMvc.perform(get("/ratings/video/{videoId}", testVideo1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetRatingsByCustomerId() throws Exception {
        mockMvc.perform(get("/ratings/customer/{customerId}", testCustomer1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testDeleteAllByVideoId() throws Exception {
        mockMvc.perform(delete("/ratings/video/all/{videoId}", testVideo1.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/ratings/video/{videoId}", testVideo1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testDeleteAllByCustomerId() throws Exception {
        mockMvc.perform(delete("/ratings/customer/all/{customerId}", testCustomer1.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/ratings/customer/{customerId}", testCustomer1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetAverageScoreByVideoId() throws Exception {
        mockMvc.perform(get("/ratings/average/{id}", testVideo1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf((double) (rating1Score + rating2Score) / 2)));
    }
}
