package com.pjsh.vrs.integration.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.CacheService;
import com.pjsh.vrs.service.provider.TestTimeProvider;
import com.pjsh.vrs.service.provider.TimeProvider;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CacheServiceIntegrationTest {

    @Autowired
    private CacheService cacheService;

    private TimeProvider timeProvider;

    @Autowired
    private VideoRepository videoRepository;

    private Video video1, video2;

    @BeforeEach
    public void setUp() {
        videoRepository.deleteAll();

        timeProvider = new TestTimeProvider(System.currentTimeMillis());
        cacheService.setTimeProvider(timeProvider);

        video1 = new Video();
        video1.setTitle("Inception");
        video1.setDirector("Christopher Nolan");
        video1.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt");
        video1.setYear(2010);
        video1.setDuration("148 min");
        video1.setGenre("Sci-Fi");
        video1.setDescription("A mind-bending thriller about dreams within dreams.");
        video1.setQuantity(5);
        videoRepository.save(video1);

        video2 = new Video();
        video2.setTitle("Titanic");
        video2.setDirector("James Cameron");
        video2.setActors("Leonardo DiCaprio, Kate Winslet");
        video2.setYear(1997);
        video2.setDuration("195 min");
        video2.setGenre("Romance");
        video2.setDescription("A tragic love story set against the backdrop of the Titanic.");
        video2.setQuantity(3);
        videoRepository.save(video2);
    }

    @AfterAll
    public void cleanUp() {
        videoRepository.deleteAll();
    }

    @Test
    public void testStoreAndRetrieveRecommendationsFromCache() {
        Long customerId = 1L;

        cacheService.storeRecommendationsInCache(customerId, List.of(video1, video2));

        List<Video> cachedVideos = cacheService.getRecommendationsFromCache(customerId);

        assertNotNull(cachedVideos);
        assertEquals(2, cachedVideos.size());
        assertEquals("Inception", cachedVideos.get(0).getTitle());
        assertEquals("Titanic", cachedVideos.get(1).getTitle());
    }

    @Test
    public void testRetrieveNonExistentCache() {
        Long customerId = 2L;

        List<Video> cachedVideos = cacheService.getRecommendationsFromCache(customerId);

        assertNull(cachedVideos);
    }

    @Test
    public void testCacheExpiration() {
        Long customerId = 1L;

        cacheService.storeRecommendationsInCache(customerId, List.of(video1, video2));

        List<Video> cachedVideos = cacheService.getRecommendationsFromCache(customerId);
        assertNotNull(cachedVideos);

        timeProvider.advanceTime(TimeUnit.MINUTES.toMillis(11));

        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customerId);
        assertNull(cachedRecommendations);
    }
}

