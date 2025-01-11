package com.pjsh.vrs.mock.service;

import static org.junit.jupiter.api.Assertions.*;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.CacheService;
import com.pjsh.vrs.service.provider.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

    @InjectMocks
    private CacheService cacheService;

    @Mock
    private TimeProvider timeProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStoreAndRetrieveRecommendations() {
        Long customerId = 1L;
        List<Video> recommendations = List.of(new Video(1L, "Title1", "Director1", "Actor1", 2021, "120min", "Drama", "Description", 5));

        cacheService.storeRecommendationsInCache(customerId, recommendations);

        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customerId);

        assertNotNull(cachedRecommendations);
        assertEquals(recommendations, cachedRecommendations);
    }

    @Test
    void testCacheExpiration() {
        Long customerId = 2L;
        List<Video> recommendations = List.of(
                new Video(2L, "Title2", "Director2", "Actor2", 2022, "130min", "Comedy", "Description", 4)
        );

        Mockito.when(timeProvider.now()).thenReturn(1000L);

        cacheService.storeRecommendationsInCache(customerId, recommendations);

        Mockito.when(timeProvider.now()).thenReturn(1000L + TimeUnit.MINUTES.toMillis(11));

        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customerId);
        assertNull(cachedRecommendations);
    }


    @Test
    void testCacheMiss() {
        Long customerId = 3L;

        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customerId);

        assertNull(cachedRecommendations);
    }
}

