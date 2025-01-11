package com.pjsh.vrs.mock.controller;

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
import org.springframework.boot.test.context.SpringBootTest;
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
public class VideoControllerTest {

    @Mock
    private VideoService videoService;

    @InjectMocks
    private VideoController videoController;

    private MockMvc mockMvc;

    private Video video1, video2;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(videoController).build();

        video1 = new Video();
        video1.setTitle("Inception");
        video1.setDirector("Christopher Nolan");
        video1.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt");
        video1.setYear(2010);
        video1.setDuration("148 min");
        video1.setGenre("Sci-Fi");
        video1.setDescription("A mind-bending thriller about dreams within dreams.");
        video1.setQuantity(5);

        video2 = new Video();
        video2.setTitle("Titanic");
        video2.setDirector("James Cameron");
        video2.setActors("Leonardo DiCaprio, Kate Winslet");
        video2.setYear(1997);
        video2.setDuration("195 min");
        video2.setGenre("Romance");
        video2.setDescription("A tragic love story set against the backdrop of the Titanic.");
        video2.setQuantity(3);
    }

    @Test
    public void testGetAllVideos() throws Exception {
        List<Video> videoList = Arrays.asList(video1, video2);
        when(videoService.getAllVideos()).thenReturn(videoList);

        mockMvc.perform(get("/videos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetVideoById() throws Exception {
        when(videoService.getVideoById(1L)).thenReturn(java.util.Optional.of(video1));

        mockMvc.perform(get("/videos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    public void testSearchVideosByTitle() throws Exception {
        List<Video> videoList = Arrays.asList(video1);
        when(videoService.searchVideosByTitle("inception")).thenReturn(videoList);

        mockMvc.perform(get("/videos/search?title=inception"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testAddVideo() throws Exception {
        when(videoService.addVideo(video1)).thenReturn(video1);

        mockMvc.perform(post("/videos")
                        .contentType("application/json")
                        .content("{ \"title\": \"Inception\", \"genre\": \"Sci-Fi\" }"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteVideo() throws Exception {
        doNothing().when(videoService).deleteVideo(1L);

        mockMvc.perform(delete("/videos/{id}", 1L))
                .andExpect(status().isOk());
    }
}

