package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoService videoService;

    private Video video1, video2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        video1 = new Video();
        video1.setTitle("Inception");
        video1.setGenre("Sci-Fi");

        video2 = new Video();
        video2.setTitle("Titanic");
        video2.setGenre("Romance");
    }

    @Test
    public void testGetAllVideos() {
        List<Video> videoList = Arrays.asList(video1, video2);
        when(videoRepository.findAll()).thenReturn(videoList);

        List<Video> result = videoService.getAllVideos();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetVideoById() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video1));

        Optional<Video> result = videoService.getVideoById(1L);
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
    }

    @Test
    public void testSearchVideosByTitle() {
        List<Video> videoList = Arrays.asList(video1);
        when(videoRepository.findByTitleContainingIgnoreCase("inception")).thenReturn(videoList);

        List<Video> result = videoService.searchVideosByTitle("inception");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testAddVideo() {
        when(videoRepository.save(video1)).thenReturn(video1);

        Video result = videoService.addVideo(video1);
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
    }

    @Test
    public void testDeleteVideo() {
        doNothing().when(videoRepository).deleteById(1L);

        videoService.deleteVideo(1L);
        verify(videoRepository, times(1)).deleteById(1L);
    }
}

