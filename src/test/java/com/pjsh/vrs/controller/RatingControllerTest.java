package com.pjsh.vrs.controller;

import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    private MockMvc mockMvc;

    private Rating rating;

    @BeforeEach
    public void setUp() {
        rating = new Rating();
        rating.setScore(5);

        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
    }

    @Test
    public void testGetRatingsByVideoId() throws Exception {
        when(ratingService.getRatingsByVideoId(1L)).thenReturn(List.of(rating));

        mockMvc.perform(get("/ratings/video/{videoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(5));
    }

    @Test
    public void testGetRatingsByCustomerId() throws Exception {
        when(ratingService.getRatingsByCustomerId(1L)).thenReturn(List.of(rating));

        mockMvc.perform(get("/ratings/customer/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(5));
    }

    @Test
    public void testAddRating() throws Exception {
        when(ratingService.addRating(any(Rating.class))).thenReturn(rating);

        mockMvc.perform(post("/ratings")
                        .contentType("application/json")
                        .content("{\"score\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    public void testDeleteRating() throws Exception {
        mockMvc.perform(delete("/ratings/{id}", 1L))
                .andExpect(status().isOk());

        verify(ratingService, times(1)).deleteRating(1L);
    }
}
