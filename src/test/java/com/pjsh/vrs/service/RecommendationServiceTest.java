package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RecommendationServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private RecommendationService recommendationService;

    private Video video1, video2, video3;
    private List<Video> allVideos;
    private List<Video> rentedVideos;

    @BeforeEach
    public void setUp() {
        video1 = new Video(1L, "Inception", "Christopher Nolan", "Leonardo DiCaprio,Joseph Gordon-Levitt", 2010, "148 min", "Sci-Fi,Thriller", "A mind-bending thriller about dreams within dreams.", 10);
        video2 = new Video(2L, "Interstellar", "Christopher Nolan", "Matthew McConaughey,Anne Hathaway", 2014, "169 min", "Sci-Fi,Drama", "A space exploration story set in a dystopian future.", 8);
        video3 = new Video(3L, "The Dark Knight", "Christopher Nolan", "Christian Bale,Heath Ledger", 2008, "152 min", "Action,Crime", "Batman faces the Joker in a battle for Gotham's soul.", 12);

        allVideos = List.of(video1, video2, video3);
        rentedVideos = List.of(video1);

        // Mocks
        when(videoRepository.findAll()).thenReturn(allVideos);
        when(videoRepository.findRentedByCustomerId(1L)).thenReturn(rentedVideos);
    }

    @Test
    public void testGetRecommendationsFromCache() {
        // Simulate cache hit
        when(cacheService.getRecommendationsFromCache(1L)).thenReturn(allVideos);

        // Call service method
        CompletableFuture<List<Video>> recommendations = recommendationService.getRecommendationsForCustomer(1L);

        // Verify cache was used
        assertThat(recommendations.join()).containsExactlyInAnyOrder(video1, video2, video3);
        verify(cacheService, times(1)).getRecommendationsFromCache(1L);
        verify(videoRepository, never()).findAll();
    }

    @Test
    public void testGenerateRecommendations() {
        // Simulate cache miss
        when(cacheService.getRecommendationsFromCache(1L)).thenReturn(null);
        when(cacheService.storeRecommendationsInCache(1L, List.of(video2, video3))).thenReturn(true);

        // Call service method
        CompletableFuture<List<Video>> recommendations = recommendationService.getRecommendationsForCustomer(1L);

        // Verify the recommendations are correctly generated
        List<Video> recommendedVideos = recommendations.join();
        assertThat(recommendedVideos).containsExactlyInAnyOrder(video2, video3);
    }
}
