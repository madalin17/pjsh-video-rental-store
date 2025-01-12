package com.pjsh.vrs.integration.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.CacheService;
import com.pjsh.vrs.service.RecommendationService;
import com.pjsh.vrs.service.RentalService;
import com.pjsh.vrs.service.provider.TimeProvider;
import com.pjsh.vrs.storage.RentalRepository;
import com.pjsh.vrs.storage.VideoRepository;
import com.pjsh.vrs.storage.CustomerRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RecommendationServiceIntegrationTest {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private TimeProvider timeProvider;

    @Autowired
    private Video video1, video2, video3, video4, video5;

    @Autowired
    private Customer customer1, customer2;

    private Video testVideo1, testVideo2, testVideo3, testVideo4, testVideo5;

    private Customer testCustomer1, testCustomer2;

    @Value("${video1.director}")
    private String video1Director;
    @Value("${video1.genre}")
    private String video1Genre;
    @Value("${noVideo.director}")
    private String noVideoDirector;
    @Value("${noVideo.genre}")
    private String noVideoGenre;

    @BeforeEach
    public void setUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));
        testVideo2 = videoRepository.save(new Video(video2));
        testVideo3 = videoRepository.save(new Video(video3));
        testVideo4 = videoRepository.save(new Video(video4));
        testVideo5 = videoRepository.save(new Video(video5));

        testCustomer1 = customerRepository.save(new Customer(customer1));
        testCustomer2 = customerRepository.save(new Customer(customer2));
    }

    @AfterAll
    public void cleanUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testGetRecommendationsForCustomer() throws Exception {
        rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId());
        rentalService.rentVideo(testCustomer1.getId(), testVideo2.getId());

        CompletableFuture<List<Video>> recommendations = recommendationService.getRecommendationsForCustomer(testCustomer1.getId());
        recommendations.join();

        assertThat(recommendations.get()).contains(testVideo4);
    }

    @Test
    public void testRecommendationsWithNoRentalHistory() throws Exception {
        List<Video> recommendations = recommendationService.getRecommendationsForCustomer(testCustomer2.getId()).get();

        assertThat(recommendations).isEmpty();
    }

    @Test
    public void testGetSimilarVideos() {
        testVideo2.setGenre(video1Genre);
        testVideo2.setDirector(video1Director);
        videoRepository.save(testVideo2);

        List<Video> similarVideos = recommendationService.findSimilarVideos(testVideo1, videoRepository.findAll());

        assertThat(similarVideos).contains(testVideo2);
    }

    @Test
    public void testRecommendationsWhenNoSimilarVideos() throws Exception {
        rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId());

        testVideo2.setGenre(noVideoGenre);
        testVideo2.setDirector(noVideoDirector);
        videoRepository.save(testVideo2);

        List<Video> recommendations = recommendationService.getRecommendationsForCustomer(testCustomer1.getId()).get();

        assertThat(recommendations).doesNotContain(testVideo2);
    }
}

