package com.pjsh.vrs.controller;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RecommendationControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    private MockMvc mockMvc;

    private List<Video> recommendedVideos;

    @BeforeEach
    public void setUp() {
        recommendedVideos = List.of(
                new Video(1L, "Inception", "Christopher Nolan", "Leonardo DiCaprio,Joseph Gordon-Levitt", 2010, "148 min", "Sci-Fi,Thriller", "A mind-bending thriller about dreams within dreams.", 10)
        );
        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
    }

    @Test
    public void testGetRecommendations_Success() throws Exception {
        // Mock the service to return recommendations
        when(recommendationService.getRecommendationsForCustomer(1L)).thenReturn(CompletableFuture.completedFuture(recommendedVideos));

        // Perform GET request and verify response
        mockMvc.perform(get("/recommendations/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[0].director").value("Christopher Nolan"));
    }

    @Test
    public void testGetRecommendations_NoContent() throws Exception {
        // Mock the service to return an empty list of recommendations
        when(recommendationService.getRecommendationsForCustomer(1L)).thenReturn(CompletableFuture.completedFuture(List.of()));

        // Perform GET request and verify response
        mockMvc.perform(get("/recommendations/{customerId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetRecommendations_InternalServerError() throws Exception {
        // Mock the service to throw an exception
        when(recommendationService.getRecommendationsForCustomer(1L)).thenThrow(new RuntimeException("Error"));

        // Perform GET request and verify response
        mockMvc.perform(get("/recommendations/{customerId}", 1L))
                .andExpect(status().isInternalServerError());
    }
}
