package com.pjsh.vrs.mock.controller;

import com.pjsh.vrs.controller.RecommendationController;
import com.pjsh.vrs.controller.future.RecommendationFutureStore;
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
import java.util.concurrent.Future;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class RecommendationControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private RecommendationFutureStore futureStore;

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
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();

        customer1Id = 1L;
    }

    @Test
    void testStartRecommendationProcess() throws Exception {
        CompletableFuture<List<Video>> mockFuture = new CompletableFuture<>();
        when(recommendationService.getRecommendationsForCustomer(customer1Id)).thenReturn(mockFuture);

        mockMvc.perform(get("/recommendations/{customerId}", customer1Id))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.length()").value(0));

        verify(recommendationService, times(1)).getRecommendationsForCustomer(customer1Id);
    }

    @Test
    void testPollingWhenRecommendationsAreReady() throws Exception {
        List<Video> recommendations = List.of(video1, video2);
        CompletableFuture<List<Video>> completedFuture = CompletableFuture.completedFuture(recommendations);

        when(recommendationService.getRecommendationsForCustomer(customer1Id)).thenReturn(completedFuture);

        mockMvc.perform(get("/recommendations/{customerId}", customer1Id))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.length()").value(0));

        CompletableFuture<List<Video>> future = new CompletableFuture<>();
        future.complete(recommendations);

        futureStore.put(customer1Id, future);
        when(futureStore.getOrDefault(customer1Id, null)).thenReturn((Future) future);

        mockMvc.perform(get("/recommendations/{customerId}", customer1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value(video1Title))
                .andExpect(jsonPath("$[1].title").value(video2Title));
    }

    @Test
    void testPollingWhenRecommendationsAreNotReady() throws Exception {
        CompletableFuture<List<Video>> incompleteFuture = new CompletableFuture<>();

        futureStore.put(customer1Id, incompleteFuture);

        mockMvc.perform(get("/recommendations/{customerId}", customer1Id))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testNoOngoingRecommendationProcess() throws Exception {
        mockMvc.perform(get("/recommendations/{customerId}", customer1Id))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
