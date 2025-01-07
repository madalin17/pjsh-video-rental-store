package com.pjsh.vrs.controller;

import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    private Review review;

    @BeforeEach
    public void setUp() {
        // Initialize mock objects
        review = new Review();
        review.setDescription("Great video!");

        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    public void testGetReviewsByVideoId() throws Exception {
        // Mock service call
        when(reviewService.getReviewsByVideoId(1L)).thenReturn(List.of(review));

        // Perform GET request and verify response
        mockMvc.perform(get("/reviews/video/{videoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Great video!"));
    }

    @Test
    public void testGetReviewsByCustomerId() throws Exception {
        // Mock service call
        when(reviewService.getReviewsByCustomerId(1L)).thenReturn(List.of(review));

        // Perform GET request and verify response
        mockMvc.perform(get("/reviews/customer/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Great video!"));
    }

    @Test
    public void testAddReview() throws Exception {
        // Mock service call
        when(reviewService.addReview(any(Review.class))).thenReturn(review);

        // Perform POST request and verify response
        mockMvc.perform(post("/reviews")
                        .contentType("application/json")
                        .content("{\"description\":\"Great video!\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Great video!"));
    }

    @Test
    public void testDeleteReview() throws Exception {
        // Perform DELETE request
        mockMvc.perform(delete("/reviews/{id}", 1L))
                .andExpect(status().isNoContent());

        // Verify that the service method was called
        verify(reviewService, times(1)).deleteReview(1L);
    }
}
