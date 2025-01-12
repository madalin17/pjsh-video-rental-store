package com.pjsh.vrs.mock.controller;

import com.pjsh.vrs.controller.RatingController;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Video;
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

    private Video video;
    private Customer customer;
    private Rating rating;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();

        video = new Video();
        video.setId(1L);
        video.setTitle("Inception");
        video.setDirector("Christopher Nolan");
        video.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt");
        video.setYear(2010);
        video.setDuration("148 min");
        video.setGenre("Sci-Fi");
        video.setDescription("A mind-bending thriller about dreams within dreams.");
        video.setQuantity(5);

        customer = new Customer();
        customer.setId(1L);
        customer.setUsername("john_doe");
        customer.setFullName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password123");

        rating = new Rating();
        rating.setId(1L);
        rating.setVideo(video);
        rating.setCustomer(customer);
        rating.setScore(5);
    }

    @Test
    public void testGetRatingsByVideoId() throws Exception {
        when(ratingService.findByVideoId(1L)).thenReturn(List.of(rating));

        mockMvc.perform(get("/ratings/video/{videoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].score").value(5));
    }

    @Test
    public void testGetRatingsByCustomerId() throws Exception {
        when(ratingService.findByCustomerId(1L)).thenReturn(List.of(rating));

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    public void testDeleteRating() throws Exception {
        doNothing().when(ratingService).deleteRating(1L);

        mockMvc.perform(delete("/ratings/{id}", 1L))
                .andExpect(status().isOk());

        verify(ratingService, times(1)).deleteRating(1L);
    }
}
