package com.pjsh.vrs.mock.controller;

import com.pjsh.vrs.controller.ReviewController;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Review;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.ReviewService;
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
public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    private Video video;
    private Customer customer;
    private Review review;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();

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

        review = new Review();
        review.setId(1L);
        review.setVideo(video);
        review.setCustomer(customer);
        review.setDescription("Great video!");
    }

    @Test
    public void testGetReviewsByVideoId() throws Exception {
        when(reviewService.getReviewsByVideoId(1L)).thenReturn(List.of(review));

        mockMvc.perform(get("/reviews/video/{videoId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Great video!"));
    }

    @Test
    public void testGetReviewsByCustomerId() throws Exception {
        when(reviewService.getReviewsByCustomerId(1L)).thenReturn(List.of(review));

        mockMvc.perform(get("/reviews/customer/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Great video!"));
    }

    @Test
    public void testAddReview() throws Exception {
        when(reviewService.addReview(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/reviews")
                        .contentType("application/json")
                        .content("{\"description\":\"Great video!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Great video!"));
    }

    @Test
    public void testDeleteReview() throws Exception {
        mockMvc.perform(delete("/reviews/{id}", 1L))
                .andExpect(status().isOk());

        verify(reviewService, times(1)).deleteReview(1L);
    }
}
