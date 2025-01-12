package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByVideoId(Long videoId);

    List<Review> findByCustomerId(Long customerId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Review r WHERE r.video.id = :videoId")
    void deleteAllByVideoId(@Param("videoId") Long videoId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Review r WHERE r.customer.id = :customerId")
    void deleteAllByCustomerId(@Param("customerId") Long customerId);
}

