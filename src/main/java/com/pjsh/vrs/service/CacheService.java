package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.provider.TimeProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    private final ConcurrentHashMap<Long, CacheEntry> cache = new ConcurrentHashMap<>();
    private static final long CACHE_EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(10);
    private TimeProvider timeProvider;

    public TimeProvider getTimeProvider() {
        return timeProvider;
    }

    public void setTimeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    /**
     * Retrieves recommendations from the cache if available and not expired.
     */
    public List<Video> getRecommendationsFromCache(Long customerId) {
        CacheEntry entry = cache.get(customerId);
        if (entry != null && !isExpired(entry)) {
            return entry.getRecommendations();
        }
        return null; // No cached recommendations or expired cache
    }

    /**
     * Stores recommendations in the cache with expiration time.
     */
    public boolean storeRecommendationsInCache(Long customerId, List<Video> recommendations) {
        cache.put(customerId, new CacheEntry(recommendations, timeProvider.now()));
        return true;
    }

    /**
     * Check if the cache entry has expired.
     */
    private boolean isExpired(CacheEntry entry) {
        return timeProvider.now() - entry.getTimestamp() > CACHE_EXPIRATION_TIME;
    }

    @Scheduled(fixedRate = 10 * 60 * 1000) // Runs every 10 minutes
    public void removeExpiredEntries() {
        cache.entrySet().removeIf(entry -> isExpired(entry.getValue()));
    }

    // CacheEntry class to store recommendations and their timestamp
    private static class CacheEntry {
        private final List<Video> recommendations;
        private final long timestamp;

        public CacheEntry(List<Video> recommendations, long timestamp) {
            this.recommendations = recommendations;
            this.timestamp = timestamp;
        }

        public List<Video> getRecommendations() {
            return recommendations;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}

