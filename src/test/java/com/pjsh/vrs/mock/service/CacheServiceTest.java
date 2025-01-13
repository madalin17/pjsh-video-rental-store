package com.pjsh.vrs.mock.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class CacheServiceTest {

    @InjectMocks
    private CacheService cacheService;

    @Mock
    private TimeProvider timeProvider;

    @Autowired
    private Video video1, video2;

    private Long customer1Id, customer2Id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer1Id = 1L;
        customer2Id = 2L;
    }

    @Test
    void testStoreAndRetrieveRecommendations() {
        List<Video> recommendations = List.of(video1);

        cacheService.storeRecommendationsInCache(customer1Id, recommendations);

        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customer1Id);

        assertThat(cachedRecommendations).isNotNull();
        assertThat(cachedRecommendations).containsExactly(video1);

        verify(timeProvider, times(2)).now();
    }

    @Test
    void testCacheExpiration() {
        List<Video> recommendations = List.of(video2);

        when(timeProvider.now()).thenReturn(1000L);

        cacheService.storeRecommendationsInCache(customer2Id, recommendations);

        when(timeProvider.now()).thenReturn(1000L + TimeUnit.MINUTES.toMillis(11));

        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customer2Id);
        assertThat(cachedRecommendations).isNull();

        verify(timeProvider, times(2)).now();
    }

    @Test
    void testCacheMiss() {
        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customer2Id);

        assertThat(cachedRecommendations).isNull();

        verifyNoInteractions(timeProvider);
    }

    @Test
    void testRemoveExpiredEntries() {
        List<Video> recommendations = List.of(video1);

        when(timeProvider.now()).thenReturn(1000L);
        cacheService.storeRecommendationsInCache(customer1Id, recommendations);

        when(timeProvider.now()).thenReturn(1000L + TimeUnit.MINUTES.toMillis(11));
        cacheService.removeExpiredEntries();

        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customer1Id);
        assertThat(cachedRecommendations).isNull();

        verify(timeProvider, times(2)).now();
    }
}
