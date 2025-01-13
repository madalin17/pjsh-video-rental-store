package com.pjsh.vrs.mock.controller;

import com.pjsh.vrs.controller.RecommendationController;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.RecommendationService;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RecommendationControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    private MockMvc mockMvc;

    @Autowired
    private Video video1, video2;

    private Long customer1Id;

    @Value("${video1.title}")
    private String video1Title;
    @Value("${video2.title}")
    private String video2Title;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();

        customer1Id = 1L;
    }

    @Test
    public void testGetRecommendationsSuccess() throws Exception {
        when(recommendationService.getRecommendationsForCustomer(customer1Id)).thenReturn(CompletableFuture.completedFuture(List.of(video1, video2)));

        mockMvc.perform(get("/recommendations/{customerId}", customer1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(video1Title))
                .andExpect(jsonPath("$[1].title").value(video2Title));
    }

    @Test
    public void testGetRecommendationsNoContent() throws Exception {
        when(recommendationService.getRecommendationsForCustomer(customer1Id)).thenReturn(CompletableFuture.completedFuture(List.of()));

        mockMvc.perform(get("/recommendations/{customerId}", customer1Id))
                .andExpect(status().isNoContent());
    }

}
