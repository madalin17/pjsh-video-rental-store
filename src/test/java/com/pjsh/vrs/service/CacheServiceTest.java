package com.pjsh.vrs.service;

import static org.junit.jupiter.api.Assertions.*;

import com.pjsh.vrs.entity.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class CacheServiceTest {

    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new CacheService();
    }

    @Test
    void testStoreAndRetrieveRecommendations() {
        Long customerId = 1L;
        List<Video> recommendations = List.of(new Video(1L, "Title1", "Director1", "Actor1", 2021, "120min", "Drama", "Description", 5));

        // Store recommendations in cache
        cacheService.storeRecommendationsInCache(customerId, recommendations);

        // Retrieve recommendations from cache
        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customerId);

        // Verify that the cache contains the recommendations
        assertNotNull(cachedRecommendations);
        assertEquals(recommendations, cachedRecommendations);
    }

    @Test
    void testCacheExpiration() throws InterruptedException {
        Long customerId = 2L;
        List<Video> recommendations = List.of(new Video(2L, "Title2", "Director2", "Actor2", 2022, "130min", "Comedy", "Description", 4));

        // Store recommendations in cache
        cacheService.storeRecommendationsInCache(customerId, recommendations);

        // Simulate a wait time that exceeds the TTL (10 minutes)
        Thread.sleep(11 * 60 * 1000);  // Wait for 11 minutes

        // Try to retrieve recommendations after expiration
        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customerId);

        // Verify that the cache has expired and no recommendations are returned
        assertNull(cachedRecommendations);
    }

    @Test
    void testCacheMiss() {
        Long customerId = 3L;

        // Try to retrieve recommendations when cache is empty
        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customerId);

        // Verify that no recommendations are returned
        assertNull(cachedRecommendations);
    }
}

