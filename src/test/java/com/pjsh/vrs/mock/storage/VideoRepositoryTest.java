package com.pjsh.vrs.mock.storage;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    private Video video1, video2;

    @BeforeEach
    public void setUp() {
        video1 = new Video();
        video1.setTitle("Inception");
        video1.setDirector("Christopher Nolan");
        video1.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt");
        video1.setYear(2010);
        video1.setDuration("148 min");
        video1.setGenre("Sci-Fi");
        video1.setDescription("A mind-bending thriller about dreams within dreams.");
        video1.setQuantity(5);
        videoRepository.save(video1);

        video2 = new Video();
        video2.setTitle("Titanic");
        video2.setDirector("James Cameron");
        video2.setActors("Leonardo DiCaprio, Kate Winslet");
        video2.setYear(1997);
        video2.setDuration("195 min");
        video2.setGenre("Romance");
        video2.setDescription("A tragic love story set against the backdrop of the Titanic.");
        video2.setQuantity(3);
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

