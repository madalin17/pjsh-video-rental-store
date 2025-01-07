package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Custom query to find all reviews for a specific video
    List<Review> findByVideoId(Long videoId);

    // Custom query to find all reviews written by a specific customer
    List<Review> findByCustomerId(Long customerId);
}

