package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    private Video video1, video2;

    @BeforeEach
    public void setUp() {
        video1 = new Video();
        video1.setTitle("Inception");
        video1.setGenre("Sci-Fi");
        videoRepository.save(video1);

        video2 = new Video();
        video2.setTitle("Titanic");
        video2.setGenre("Romance");
        videoRepository.save(video2);
    }

    @Test
    public void testFindByTitleContainingIgnoreCase() {
        List<Video> videos = videoRepository.findByTitleContainingIgnoreCase("inception");
        assertEquals(1, videos.size());
        assertTrue(videos.get(0).getTitle().contains("Inception"));
    }

    @Test
    public void testFindByGenre() {
        List<Video> videos = videoRepository.findByGenre("Sci-Fi");
        assertEquals(1, videos.size());
        assertTrue(videos.get(0).getGenre().equals("Sci-Fi"));
    }
}

