package com.pjsh.vrs.controller;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/{customerId}")
    public ResponseEntity<List<Video>> getRecommendations(@PathVariable Long customerId) {
        try {
            Future<List<Video>> recommendationsFuture = recommendationService.getRecommendationsForCustomer(customerId);

            List<Video> recommendations = recommendationsFuture.get();

            if (recommendations != null && !recommendations.isEmpty()) {
                return new ResponseEntity<>(recommendations, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (InterruptedException | ExecutionException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}