package com.pjsh.vrs.mock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.controller.VideoController;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.VideoService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class VideoControllerTest {

    @Mock
    private VideoService videoService;

    @InjectMocks
    private VideoController videoController;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private Video video1, video2;

    private Long video1Id, video2Id;

    @Value("${video1.title}")
    private String video1Title;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(videoController).build();

        video1Id = 1L;
        video2Id = 2L;
    }

    @Test
    void testGetAllVideos() throws Exception {
        List<Video> videoList = Arrays.asList(video1, video2);
        when(videoService.getAllVideos()).thenReturn(videoList);

        mockMvc.perform(get("/videos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetVideoById() throws Exception {
        when(videoService.getVideoById(1L)).thenReturn(java.util.Optional.of(video1));

        mockMvc.perform(get("/videos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(video1Title));
    }

    @Test
    void testSearchVideosByTitle() throws Exception {
        when(videoService.searchVideosByTitle(video1Title)).thenReturn(Arrays.asList(video1));

        mockMvc.perform(get("/videos/search?title=" + video1Title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testAddVideo() throws Exception {
        when(videoService.addVideo(any(Video.class))).thenReturn(video1);

        mockMvc.perform(post("/videos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(video1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    void testDeleteVideo() throws Exception {
        doNothing().when(videoService).deleteVideo(1L);

        mockMvc.perform(delete("/videos/{id}", video1Id))
                .andExpect(status().isOk());

        verify(videoService, times(1)).deleteVideo(video1Id);
    }
}
