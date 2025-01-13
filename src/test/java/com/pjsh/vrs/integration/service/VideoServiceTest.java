package com.pjsh.vrs.integration.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class VideoServiceTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private Video video1, video2, video3;

    private Video testVideo1, testVideo2;

    @BeforeEach
    public void setUp() {
        videoRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));
        testVideo2 = videoRepository.save(new Video(video2));
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
        Video foundVideo = videoRepository.findById(testVideo1.getId()).orElse(null);

        assertThat(foundVideo).isNotNull();
        assertThat(foundVideo.getTitle()).isEqualTo("Inception");
        assertThat(foundVideo.getQuantity()).isEqualTo(5);
    }

    @Test
    public void testSaveNewVideo() {
        Video testVideo3 = videoRepository.save(new Video(video3));

        assertThat(testVideo3.getId()).isNotNull();
        assertThat(videoRepository.findAll()).hasSize(3);
    }

    @Test
    public void testUpdateVideoQuantity() {
        Video videoToUpdate = videoRepository.findById(testVideo2.getId()).orElseThrow();
        videoToUpdate.setQuantity(10);

        videoRepository.save(videoToUpdate);

        Video updatedVideo = videoRepository.findById(testVideo2.getId()).orElseThrow();
        assertThat(updatedVideo.getQuantity()).isEqualTo(10);
    }

    @Test
    public void testDeleteVideo() {
        System.out.println(videoRepository.findAll());
        videoRepository.deleteById(testVideo1.getId());

        List<Video> videos = videoRepository.findAll();
        assertThat(videos).hasSize(1);
        assertThat(videos).extracting(Video::getTitle).containsExactly("Titanic");
    }
}
