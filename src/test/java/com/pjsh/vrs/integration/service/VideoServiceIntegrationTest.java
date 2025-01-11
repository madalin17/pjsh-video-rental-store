package com.pjsh.vrs.integration.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VideoServiceIntegrationTest {

    @Autowired
    private VideoRepository videoRepository;

    private Video video1, video2;

    @BeforeEach
    public void setUp() {
        videoRepository.deleteAll();

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

    @AfterAll
    public void cleanUp() {
        videoRepository.deleteAll();
    }

    @Test
    public void testFindAllVideos() {
        List<Video> videos = videoRepository.findAll();

        assertThat(videos).hasSize(2);
        assertThat(videos).extracting(Video::getTitle).containsExactlyInAnyOrder("Inception", "Titanic");
    }

    @Test
    public void testFindVideoById() {
        Video foundVideo = videoRepository.findById(video1.getId()).orElse(null);

        assertThat(foundVideo).isNotNull();
        assertThat(foundVideo.getTitle()).isEqualTo("Inception");
        assertThat(foundVideo.getQuantity()).isEqualTo(5);
    }

    @Test
    public void testSaveNewVideo() {
        Video newVideo = new Video();
        newVideo.setTitle("Avatar");
        newVideo.setDirector("James Cameron");
        newVideo.setActors("Sam Worthington, Zoe Saldana");
        newVideo.setYear(2009);
        newVideo.setDuration("162 min");
        newVideo.setGenre("Sci-Fi");
        newVideo.setDescription("A visually stunning epic set on the alien world of Pandora.");
        newVideo.setQuantity(4);

        Video savedVideo = videoRepository.save(newVideo);

        assertThat(savedVideo.getId()).isNotNull();
        assertThat(videoRepository.findAll()).hasSize(3);
    }

    @Test
    public void testUpdateVideoQuantity() {
        Video videoToUpdate = videoRepository.findById(video2.getId()).orElseThrow();
        videoToUpdate.setQuantity(10);

        videoRepository.save(videoToUpdate);

        Video updatedVideo = videoRepository.findById(video2.getId()).orElseThrow();
        assertThat(updatedVideo.getQuantity()).isEqualTo(10);
    }

    @Test
    public void testDeleteVideo() {
        videoRepository.deleteById(video1.getId());

        List<Video> videos = videoRepository.findAll();
        assertThat(videos).hasSize(1);
        assertThat(videos).extracting(Video::getTitle).containsExactly("Titanic");
    }
}
