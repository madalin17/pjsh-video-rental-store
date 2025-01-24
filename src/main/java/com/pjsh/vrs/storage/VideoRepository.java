package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findByTitle(String title);

    List<Video> findByTitleContainingIgnoreCase(String title);

    List<Video> findByGenre(String genre);

    @Query("SELECT v FROM Video v JOIN Rental r ON v.id = r.video.id WHERE r.customer.id = :customerId")
    List<Video> findRentedByCustomerId(@Param("customerId") Long customerId);


}
