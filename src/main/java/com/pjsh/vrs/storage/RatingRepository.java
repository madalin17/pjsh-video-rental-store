package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    // Custom query to find all ratings for a specific video
    List<Rating> findByVideoId(Long videoId);

    // Custom query to find all ratings given by a specific customer
    List<Rating> findByCustomerId(Long customerId);
}

