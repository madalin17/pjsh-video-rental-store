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

    /**
     * Endpoint to get personalized recommendations for a customer.
     * @param customerId the ID of the customer
     * @return a list of recommended videos
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<List<Video>> getRecommendations(@PathVariable Long customerId) {
        try {
            // Fetch recommendations asynchronously
            Future<List<Video>> recommendationsFuture = recommendationService.getRecommendationsForCustomer(customerId);

            // Wait for the result of the async operation
            List<Video> recommendations = recommendationsFuture.get();

            // Return the recommendations as a JSON response
            if (recommendations != null && !recommendations.isEmpty()) {
                return new ResponseEntity<>(recommendations, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No recommendations found
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle errors gracefully, maybe log the exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}