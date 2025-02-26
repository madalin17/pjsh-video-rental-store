package com.pjsh.vrs.controller;

import com.pjsh.vrs.controller.future.RecommendationFutureStore;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private RecommendationFutureStore futureStore;

    @GetMapping("/{customerId}")
    public ResponseEntity<List<Video>> getRecommendations(@PathVariable Long customerId) {
        CompletableFuture<List<Video>> recommendationsFuture = (CompletableFuture<List<Video>>)
                futureStore.getOrDefault(customerId, null);

        if (recommendationsFuture == null) {
            recommendationsFuture = recommendationService.getRecommendationsForCustomer(customerId);

            futureStore.put(customerId, recommendationsFuture);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Collections.emptyList());
        } else if (recommendationsFuture.isDone()) {
            futureStore.remove(customerId);

            try {
                List<Video> recommendations = recommendationsFuture.get();

                if (recommendations == null || recommendations.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
                }

                return ResponseEntity.status(HttpStatus.OK).body(recommendations);
            } catch (InterruptedException | ExecutionException e) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
            }
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Collections.emptyList());
    }
}