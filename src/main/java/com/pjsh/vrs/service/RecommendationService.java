package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CacheService cacheService;

    /**
     * Generate personalized recommendations for a customer based on rental history.
     */
    @Async
    public CompletableFuture<List<Video>> getRecommendationsForCustomer(Long customerId) {
        // First, check the cache for recommendations
        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customerId);
        if (cachedRecommendations != null) {
            // Return cached recommendations if available
            return CompletableFuture.completedFuture(cachedRecommendations);
        }

        // Fetch all videos and the customer's rented videos asynchronously
        List<Video> allVideos = videoRepository.findAll();
        List<Video> rentedVideos = videoRepository.findRentedByCustomerId(customerId); // Find rentals by customerId

        // Create a set of similar videos for all rented videos
        Set<Video> recommendations = new HashSet<>();
        for (Video rentedVideo : rentedVideos) {
            recommendations.addAll(findSimilarVideos(rentedVideo, allVideos));  // Pass rented videos to find similar ones
        }

        // Remove already rented videos from recommendations
        recommendations.removeAll(rentedVideos);

        // Return recommendations as a sorted list (e.g., by similarity or popularity)
        List<Video> recommendedList = recommendations.stream().limit(10).collect(Collectors.toList());

        // Store the recommendations in the cache
        cacheService.storeRecommendationsInCache(customerId, recommendedList);

        // Return the recommendations asynchronously
        return CompletableFuture.completedFuture(recommendedList);
    }

    /**
     * Find videos similar to a given video based on attributes.
     */
    protected List<Video> findSimilarVideos(Video video, List<Video> allVideos) {
        return allVideos.stream()
                .filter(v -> !v.getId().equals(video.getId())) // Exclude the original video
                .filter(v -> isSimilar(video, v)) // Apply similarity logic
                .collect(Collectors.toList());
    }

    /**
     * Define similarity logic between two videos.
     */
    private boolean isSimilar(Video v1, Video v2) {
        int score = 0;

        // Similar genre
        if (v1.getGenre().equalsIgnoreCase(v2.getGenre())) score += 5;

        // Same director
        if (v1.getDirector().equalsIgnoreCase(v2.getDirector())) score += 3;

        // Overlapping actors
        List<String> actors1 = Arrays.asList(v1.getActors().split(","));
        List<String> actors2 = Arrays.asList(v2.getActors().split(","));
        if (!Collections.disjoint(actors1, actors2)) score += 2;

        return score >= 5; // Return true if the similarity score is above the threshold
    }
}
