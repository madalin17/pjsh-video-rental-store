package com.pjsh.vrs.mock.service;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.VideoService;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoService videoService;

    @Autowired
    private Video video1, video2;

    private Long video1Id, video2Id;

    @Value("${rating1.score}")
    private int rating1Score;
    @Value("${rating2.score}")
    private int rating2Score;

    @BeforeEach
    void setUp() {
        video1Id = 1L;
        video2Id = 2L;
    }

    @Test
    void testGetAllVideos() {
        when(videoRepository.findAll()).thenReturn(List.of(video1, video2));

        List<Video> videos = videoService.getAllVideos();
        assertNotNull(videos);
        assertEquals(2, videos.size());
        assertEquals(video1.getTitle(), videos.get(0).getTitle());
        assertEquals(video2.getTitle(), videos.get(1).getTitle());
    }

    @Test
    void testGetVideoById() {
        when(videoRepository.findById(video1Id)).thenReturn(Optional.of(video1));

        Optional<Video> video = videoService.getVideoById(video1Id);
        assertTrue(video.isPresent());
        assertEquals(video1.getTitle(), video.get().getTitle());
        assertEquals(video1.getGenre(), video.get().getGenre());
    }

    @Test
    void testSearchVideosByTitle() {
        when(videoRepository.findByTitleContainingIgnoreCase(video1.getTitle())).thenReturn(List.of(video1));

        List<Video> videos = videoService.searchVideosByTitle(video1.getTitle());
        assertNotNull(videos);
        assertEquals(1, videos.size());
        assertEquals(video1.getTitle(), videos.get(0).getTitle());
    }

    @Test
    void testAddVideo() {
        when(videoRepository.save(video2)).thenReturn(video2);

        Video result = videoService.addVideo(video2);
        assertNotNull(result);
        assertEquals(video2.getTitle(), result.getTitle());
        assertEquals(video2.getGenre(), result.getGenre());
    }

    @Test
    void testDeleteVideo() {
        doNothing().when(videoRepository).deleteById(video1Id);

        videoService.deleteVideo(video1Id);
        verify(videoRepository, times(1)).deleteById(video1Id);
    }
}
