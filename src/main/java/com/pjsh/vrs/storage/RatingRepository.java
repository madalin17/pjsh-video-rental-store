package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByVideoId(Long videoId);

    List<Rating> findByCustomerId(Long customerId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Rating r WHERE r.video.id = :videoId")
    void deleteAllByVideoId(@Param("videoId") Long videoId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Rating r WHERE r.customer.id = :customerId")
    void deleteAllByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.video.id = :videoId")
    Double calculateAverageScoreByVideoId(Long videoId);
}

