package com.pjsh.vrs.mock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.controller.RatingController;
import com.pjsh.vrs.entity.Rating;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.service.RatingService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Video video1, video2;

    @Autowired
    private Customer customer1, customer2;

    private Rating rating1, rating2;

    private Long video1Id, video2Id, customer1Id, customer2Id, rating1Id;

    @Value("${rating1.score}")
    private int rating1Score;
    @Value("${rating2.score}")
    private int rating2Score;
    @Value("${rating3.score}")
    private int rating3Score;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();

        video1Id = 1L;
        video2Id = 2L;
        customer1Id = 1L;
        customer2Id = 2L;
        rating1Id = 1L;

        video1.setId(video1Id);
        video2.setId(video2Id);
        customer1.setId(customer1Id);
        customer2.setId(customer2Id);

        rating1 = new Rating(video1, customer1, rating1Score);
        rating2 = new Rating(video1, customer2, rating2Score);

        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rating1)))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rating2)))
                .andDo(print())
                .andExpect(status().isOk());

        when(ratingService.findByVideoId(video1Id)).thenReturn(List.of(rating1, rating2));
        when(ratingService.findByCustomerId(customer1Id)).thenReturn(List.of(rating1));
        when(ratingService.getAverageScoreByVideoId(video1Id)).thenReturn((double) (rating1Score + rating2Score) / 2);
    }

    @Test
    public void testAddRating() throws Exception {
        Rating rating3 = new Rating(video2, customer1, rating3Score);

        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rating3)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteRating() throws Exception {
        mockMvc.perform(delete("/ratings/{id}", rating1Id))
                .andDo(print())
                .andExpect(status().isOk());

        when(ratingService.findByVideoId(video1Id)).thenReturn(List.of(rating2));

        mockMvc.perform(get("/ratings/video/{videoId}", video1Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetRatingsByVideoId() throws Exception {
        mockMvc.perform(get("/ratings/video/{videoId}", video1Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetRatingsByCustomerId() throws Exception {
        mockMvc.perform(get("/ratings/customer/{customerId}", customer1Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testDeleteAllByVideoId() throws Exception {
        mockMvc.perform(delete("/ratings/video/all/{videoId}", video1Id))
                .andDo(print())
                .andExpect(status().isOk());

        when(ratingService.findByVideoId(video1Id)).thenReturn(List.of());

        mockMvc.perform(get("/ratings/video/{videoId}", video1Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testDeleteAllByCustomerId() throws Exception {
        mockMvc.perform(delete("/ratings/customer/all/{customerId}", customer1Id))
                .andDo(print())
                .andExpect(status().isOk());

        when(ratingService.findByCustomerId(customer1Id)).thenReturn(List.of());

        mockMvc.perform(get("/ratings/customer/{customerId}", customer1Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetAverageScoreByVideoId() throws Exception {
        mockMvc.perform(get("/ratings/average/{id}", video1Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf((double) (rating1Score + rating2Score) / 2)));
    }
}
