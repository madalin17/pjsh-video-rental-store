package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CacheService cacheService;

    @Async
    public CompletableFuture<List<Video>> getRecommendationsForCustomer(Long customerId) {
        List<Video> cachedRecommendations = cacheService.getRecommendationsFromCache(customerId);
        if (cachedRecommendations != null) {
            return CompletableFuture.completedFuture(cachedRecommendations);
        }

        List<Video> allVideos = videoRepository.findAll();
        List<Video> rentedVideos = videoRepository.findRentedByCustomerId(customerId);

        Set<Video> recommendations = new HashSet<>();
        for (Video rentedVideo : rentedVideos) {
            recommendations.addAll(findSimilarVideos(rentedVideo, allVideos));
        }

        recommendations.removeAll(rentedVideos);

        List<Video> recommendedList = recommendations.stream().limit(10).collect(Collectors.toList());

        cacheService.storeRecommendationsInCache(customerId, recommendedList);

        return CompletableFuture.completedFuture(recommendedList);
    }

//    @Async
//    public CompletableFuture<List<Video>> getRecommendationsForCustomer(Long customerId) {
//        return CompletableFuture.supplyAsync(() -> cacheService.getRecommendationsFromCache(customerId))
//                .thenCompose(cachedRecommendations -> {
//                    if (cachedRecommendations != null) {
//                        return CompletableFuture.completedFuture(cachedRecommendations);
//                    }
//
//                    CompletableFuture<List<Video>> allVideosFuture = CompletableFuture.supplyAsync(() -> videoRepository.findAll());
//                    CompletableFuture<List<Video>> rentedVideosFuture = CompletableFuture.supplyAsync(() -> videoRepository.findRentedByCustomerId(customerId));
//
//                    return allVideosFuture.thenCombine(rentedVideosFuture, (allVideos, rentedVideos) -> {
//                        Set<Video> recommendations = new HashSet<>();
//                        for (Video rentedVideo : rentedVideos) {
//                            recommendations.addAll(findSimilarVideos(rentedVideo, allVideos));
//                        }
//                        rentedVideos.forEach(recommendations::remove);
//
//                        List<Video> recommendedList = recommendations.stream().limit(10).toList();
//
//                        CompletableFuture.runAsync(() -> cacheService.storeRecommendationsInCache(customerId, recommendedList));
//                        System.out.println(recommendedList);
//
//                        return recommendedList;
//                    });
//                });
//    }

    public List<Video> findSimilarVideos(Video video, List<Video> allVideos) {
        var similarVideos = allVideos.stream()
                .filter(v -> !v.getId().equals(video.getId()))
                .filter(v -> isSimilar(video, v))
                .toList();
        System.out.println("Found similar videos: " + similarVideos);
        return allVideos.stream()
                .filter(v -> !v.getId().equals(video.getId()))
                .filter(v -> isSimilar(video, v))
                .toList();
    }

    private boolean isSimilar(Video v1, Video v2) {
        int score = 0;

        if (v1.getGenre().equalsIgnoreCase(v2.getGenre())) score += 5;

        if (v1.getDirector().equalsIgnoreCase(v2.getDirector())) score += 3;

        List<String> actors1 = Arrays.asList(v1.getActors().split(","));
        List<String> actors2 = Arrays.asList(v2.getActors().split(","));
        if (!Collections.disjoint(actors1, actors2)) score += 2;

        return score >= 5;
    }
}
