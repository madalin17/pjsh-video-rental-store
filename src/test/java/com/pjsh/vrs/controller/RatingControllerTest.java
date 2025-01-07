package com.pjsh.vrs.controller;

import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.service.RatingService;
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
public class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    private MockMvc mockMvc;

    private Rating rating;

    @BeforeEach
    public void setUp() {
        // Initialize mock objects
        rating = new Rating();
        rating.setScore(5);

        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
    }

    @Test
    public void testGetRatingsByVideoId() throws Exception {
        // Mock service call
        when(ratingService.getRatingsByVideoId(1L)).thenReturn(List.of(rating));

        // Perform GET request and verify response
        mockMvc.perform(get("/ratings/video/{videoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(5));
    }

    @Test
    public void testGetRatingsByCustomerId() throws Exception {
        // Mock service call
        when(ratingService.getRatingsByCustomerId(1L)).thenReturn(List.of(rating));

        // Perform GET request and verify response
        mockMvc.perform(get("/ratings/customer/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(5));
    }

    @Test
    public void testAddRating() throws Exception {
        // Mock service call
        when(ratingService.addRating(any(Rating.class))).thenReturn(rating);

        // Perform POST request and verify response
        mockMvc.perform(post("/ratings")
                        .contentType("application/json")
                        .content("{\"score\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    public void testDeleteRating() throws Exception {
        // Perform DELETE request
        mockMvc.perform(delete("/ratings/{id}", 1L))
                .andExpect(status().isNoContent());

        // Verify that the service method was called
        verify(ratingService, times(1)).deleteRating(1L);
    }
}
