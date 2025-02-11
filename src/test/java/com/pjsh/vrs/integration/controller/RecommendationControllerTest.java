package com.pjsh.vrs.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.controller.future.RecommendationFutureStore;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.RecommendationService;
import com.pjsh.vrs.service.RentalService;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RentalRepository;
import com.pjsh.vrs.storage.VideoRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private RecommendationFutureStore futureStore;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private Video video1, video2, video3, video4, video5;

    @Autowired
    private Customer customer1, customer2;

    private Video testVideo1, testVideo2, testVideo3, testVideo4, testVideo5;

    private Customer testCustomer1;

    private Rating testRating1;

    @Value("${video1.title}")
    private String video1Title;
    @Value("${video2.title}")
    private String video2Title;
    @Value("${video3.title}")
    private String video3Title;
    @Value("${video4.title}")
    private String video4Title;
    @Value("${video5.title}")
    private String video5Title;
    @Value("${rating1.score}")
    private Integer rating1Score;

    @BeforeEach
    void setUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));
        testVideo2 = videoRepository.save(new Video(video2));
        testVideo3 = videoRepository.save(new Video(video3));
        testVideo4 = videoRepository.save(new Video(video4));
        testVideo5 = videoRepository.save(new Video(video5));

        testCustomer1 = customerRepository.save(new Customer(customer1));
    }

    @AfterEach
    void tearDown() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void testGetRecommendationsSuccess() throws Exception {
        rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId());
        rentalService.rentVideo(testCustomer1.getId(), testVideo2.getId());

        mockMvc.perform(get("/recommendations/{customerId}", testCustomer1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());

        Thread.sleep(1000);

        mockMvc.perform(get("/recommendations/{customerId}", testCustomer1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testGetRecommendationsNoContent() throws Exception {
        mockMvc.perform(get("/recommendations/{customerId}", testCustomer1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());

        Thread.sleep(1000);

        mockMvc.perform(get("/recommendations/{customerId}", testCustomer1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
