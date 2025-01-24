package com.pjsh.vrs.mock.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.CacheService;
import com.pjsh.vrs.service.RecommendationService;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RecommendationServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CacheService cacheService;

    @Spy
    @InjectMocks
    private RecommendationService recommendationService;

    @Autowired
    private Video video1, video2, video3, video4;

    private Long video1Id, video2Id, video3Id, video4Id, customer1Id;

    private List<Video> allVideos, recommendedVideos, rentedVideos;

    @BeforeEach
    public void setUp() {
        video1Id = 1L;
        video2Id = 2L;
        video3Id = 3L;
        video4Id = 4L;
        customer1Id = 1L;

        allVideos = List.of(video1, video2, video3, video4);
        recommendedVideos = List.of(video4);
        rentedVideos = List.of(video1);

        when(videoRepository.findAll()).thenReturn(allVideos);
        when(videoRepository.findRentedByCustomerId(customer1Id)).thenReturn(rentedVideos);
    }

    @Test
    public void testGetRecommendationsFromCache() {
        when(cacheService.getRecommendationsFromCache(customer1Id)).thenReturn(recommendedVideos);

        CompletableFuture<List<Video>> recommendations = recommendationService.getRecommendationsForCustomer(customer1Id);

        assertThat(recommendations.join()).containsExactlyInAnyOrder(video3);
        verify(cacheService, times(1)).getRecommendationsFromCache(customer1Id);
        verify(videoRepository, never()).findAll();
    }

    @Test
    public void testGenerateRecommendations() throws Exception {
        when(cacheService.getRecommendationsFromCache(customer1Id)).thenReturn(null);

        doReturn(recommendedVideos)
                .when(recommendationService).findSimilarVideos(eq(video1), anyList());

        CompletableFuture<List<Video>> recommendations = recommendationService.getRecommendationsForCustomer(customer1Id);

        List<Video> recommendedVideos = recommendations.join();
        assertThat(recommendedVideos).containsExactlyInAnyOrder(video3);

        verify(videoRepository, times(1)).findAll();
        verify(cacheService, times(1)).storeRecommendationsInCache(eq(customer1Id), argThat(list ->
                list.contains(video3) && list.size() == 1
        ));
    }

    @Test
    public void testNoRecommendationsGenerated() {
        when(cacheService.getRecommendationsFromCache(customer1Id)).thenReturn(null);
        doReturn(List.of()).when(recommendationService).findSimilarVideos(eq(video1), anyList());

        CompletableFuture<List<Video>> recommendations = recommendationService.getRecommendationsForCustomer(customer1Id);

        List<Video> recommendedVideos = recommendations.join();
        assertThat(recommendedVideos).isEmpty();

        verify(videoRepository, times(1)).findAll();
        verify(cacheService, times(1)).storeRecommendationsInCache(eq(customer1Id), eq(List.of()));
    }

    @Test
    public void testFindSimilarVideos() {
        video1.setId(video1Id);
        video2.setId(video2Id);
        video3.setId(video3Id);
        video4.setId(video4Id);
        List<Video> similarVideos = recommendationService.findSimilarVideos(video1, allVideos);

        assertThat(similarVideos).contains(video4);
        assertThat(similarVideos).doesNotContain(video1, video2);
    }
}
