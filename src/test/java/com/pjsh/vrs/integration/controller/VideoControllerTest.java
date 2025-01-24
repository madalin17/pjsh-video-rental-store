package com.pjsh.vrs.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Video video1, video2, video3;

    private Video testVideo1, testVideo2;

    @Value("${video1.title}")
    private String video1Title;
    @Value("${video2.title}")
    private String video2Title;
    @Value("${video3.title}")
    private String video3Title;

    @BeforeEach
    void setUp() {
        videoRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));
        testVideo2 = videoRepository.save(new Video(video2));
    }

    @AfterAll
    void cleanUp() {
        videoRepository.deleteAll();
    }

    @Test
    void testGetAllVideos() throws Exception {
        mockMvc.perform(get("/videos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value(video1Title))
                .andExpect(jsonPath("$[1].title").value(video2Title));
    }

    @Test
    void testGetVideoById() throws Exception {
        mockMvc.perform(get("/videos/{id}", testVideo1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(video1Title));
    }

    @Test
    void testSearchVideosByTitle() throws Exception {
        mockMvc.perform(get("/videos/search").param("title", video1Title))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(video1Title));
    }

    @Test
    void testGetVideosByGenre() throws Exception {
        mockMvc.perform(get("/videos/genre/{genre}", video1.getGenre()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(video1Title));
    }

    @Test
    void testAddVideo() throws Exception {
        mockMvc.perform(post("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video3)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(video3Title));

        List<Video> allVideos = videoRepository.findAll();
        assert allVideos.size() == 3;
    }

    @Test
    void testDeleteVideo() throws Exception {
        mockMvc.perform(delete("/videos/{id}", testVideo1.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/videos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(video2Title));
    }
}
