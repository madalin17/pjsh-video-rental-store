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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class CacheServiceTest {

    @Autowired
    private CacheService cacheService;

    private TimeProvider timeProvider;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private Video video1, video2, video3;

    private Video testVideo1, testVideo2;

    @Value("${video1.title}")
    private String video1Title;
    @Value("${video2.title}")
    private String video2Title;

    @BeforeEach
    public void setUp() {
        videoRepository.deleteAll();

        timeProvider = new TestTimeProvider(System.currentTimeMillis());
        cacheService.setTimeProvider(timeProvider);

        testVideo1 = videoRepository.save(new Video(video1));
        testVideo2 = videoRepository.save(new Video(video2));
    }

    @AfterAll
    public void cleanUp() {
        videoRepository.deleteAll();
    }

    @Test
    public void testStoreAndRetrieveRecommendationsFromCache() {
        Long customerId = 1L;

        cacheService.storeRecommendationsInCache(customerId, List.of(testVideo1, testVideo2));

        List<Video> cachedVideos = cacheService.getRecommendationsFromCache(customerId);

        assertNotNull(cachedVideos);
        assertEquals(2, cachedVideos.size());
        assertEquals(video1Title, cachedVideos.get(0).getTitle());
        assertEquals(video2Title, cachedVideos.get(1).getTitle());
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

        cacheService.storeRecommendationsInCache(customerId, List.of(testVideo1, testVideo2));

        List<Video> cachedVideos = cacheService.getRecommendationsFromCache(customerId);
        assertNotNull(cachedVideos);

        timeProvider.advanceTime(TimeUnit.MINUTES.toMillis(11));

        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customerId);
        assertNull(cachedRecommendations);
    }
}

