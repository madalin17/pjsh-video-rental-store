package com.pjsh.vrs.mock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.controller.ReviewController;
import com.pjsh.vrs.controller.requests.ReviewRequest;
import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.service.CustomerService;
import com.pjsh.vrs.service.RatingService;
import com.pjsh.vrs.service.ReviewService;
import com.pjsh.vrs.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class ReviewControllerTest {

    @Mock
    private VideoService videoService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Video video1, video2;

    @Autowired
    private Customer customer1, customer2;

    private Review review1, review2;

    private Long video1Id, video2Id, customer1Id, customer2Id, review1Id, review2Id;

    @Value("${review1.description}")
    private String review1Description;
    @Value("${review2.description}")
    private String review2Description;
    @Value("${review3.description}")
    private String review3Description;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();

        video1Id = 1L;
        video2Id = 2L;
        customer1Id = 1L;
        customer2Id = 2L;
        review1Id = 1L;

        review1 = new Review(video1, customer1, review1Description);
        review2 = new Review(video1, customer2, review2Description);

        when(reviewService.findByVideoId(video1Id)).thenReturn(List.of(review1, review2));
        when(reviewService.findByCustomerId(customer1Id)).thenReturn(List.of(review1));
    }

    @Test
    void testAddReview() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setTitle(video2.getTitle());
        request.setUsername(customer2.getUsername());
        request.setDescription(review3Description);

        when(videoService.getByTitle(video2.getTitle())).thenReturn(video2);
        when(customerService.getByUsername(customer2.getUsername())).thenReturn(customer2);

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteReview() throws Exception {
        mockMvc.perform(delete("/reviews/{id}", review1Id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetReviewsByVideoId() throws Exception {
        mockMvc.perform(get("/reviews/video/{videoId}", video1Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value(review1Description))
                .andExpect(jsonPath("$[1].description").value(review2Description));
    }

    @Test
    void testGetReviewsByCustomerId() throws Exception {
        mockMvc.perform(get("/reviews/customer/{customerId}", customer1Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value(review1Description));
    }

    @Test
    void testDeleteAllByVideoId() throws Exception {
        mockMvc.perform(delete("/reviews/video/all/{videoId}", video1Id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAllByCustomerId() throws Exception {
        mockMvc.perform(delete("/reviews/customer/all/{customerId}", customer1Id))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
