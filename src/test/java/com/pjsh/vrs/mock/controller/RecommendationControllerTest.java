package com.pjsh.vrs.mock.controller;

import com.pjsh.vrs.controller.RecommendationController;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.RecommendationService;
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
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RecommendationControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    private MockMvc mockMvc;

    private Video video;

    private List<Video> recommendedVideos;

    @BeforeEach
    public void setUp() {
        video = new Video();
        video.setTitle("Inception");
        video.setDirector("Christopher Nolan");
        video.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt");
        video.setYear(2010);
        video.setDuration("148 min");
        video.setGenre("Sci-Fi");
        video.setDescription("A mind-bending thriller about dreams within dreams.");
        video.setQuantity(5);

        recommendedVideos = List.of(video);

        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
    }

    @Test
    public void testGetRecommendations_Success() throws Exception {
        when(recommendationService.getRecommendationsForCustomer(1L)).thenReturn(CompletableFuture.completedFuture(recommendedVideos));

        mockMvc.perform(get("/recommendations/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[0].director").value("Christopher Nolan"));
    }

    @Test
    public void testGetRecommendations_NoContent() throws Exception {
        when(recommendationService.getRecommendationsForCustomer(1L)).thenReturn(CompletableFuture.completedFuture(List.of()));

        mockMvc.perform(get("/recommendations/{customerId}", 1L))
                .andExpect(status().isNoContent());
    }

}
