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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    private Video video1, video2, video3, video4, video5;
    private Customer customer1, customer2;

    @BeforeEach
    public void setUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        video1 = new Video();
        video1.setTitle("Inception");
        video1.setDirector("Christopher Nolan");
        video1.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt");
        video1.setYear(2010);
        video1.setDuration("148 min");
        video1.setGenre("Sci-Fi");
        video1.setDescription("A mind-bending thriller about dreams within dreams.");
        video1.setQuantity(5);
        videoRepository.save(video1);

        video2 = new Video();
        video2.setTitle("Titanic");
        video2.setDirector("James Cameron");
        video2.setActors("Leonardo DiCaprio, Kate Winslet");
        video2.setYear(1997);
        video2.setDuration("195 min");
        video2.setGenre("Romance");
        video2.setDescription("A tragic love story set against the backdrop of the Titanic.");
        video2.setQuantity(3);
        videoRepository.save(video2);

        video3 = new Video();
        video3.setTitle("The Dark Knight");
        video3.setDirector("Christopher Nolan");
        video3.setActors("Christian Bale, Heath Ledger, Aaron Eckhart");
        video3.setYear(2008);
        video3.setDuration("152 min");
        video3.setGenre("Action");
        video3.setDescription("A hero faces a criminal mastermind in Gotham City.");
        video3.setQuantity(4);
        videoRepository.save(video3);

        video4 = new Video();
        video4.setTitle("Avatar");
        video4.setDirector("James Cameron");
        video4.setActors("Sam Worthington, Zoe Saldana, Sigourney Weaver");
        video4.setYear(2009);
        video4.setDuration("162 min");
        video4.setGenre("Sci-Fi");
        video4.setDescription("A paraplegic marine dispatched to the moon Pandora on a unique mission.");
        video4.setQuantity(5);
        videoRepository.save(video4);

        video5 = new Video();
        video5.setTitle("The Matrix");
        video5.setDirector("Lana Wachowski");
        video5.setActors("Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss");
        video5.setYear(1999);
        video5.setDuration("136 min");
        video5.setGenre("Sci-Fi");
        video5.setDescription("A hacker discovers a reality-bending truth about the world.");
        video5.setQuantity(6);
        videoRepository.save(video5);

        customer1 = new Customer();
        customer1.setUsername("john_doe");
        customer1.setFullName("John Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setPassword("password123");
        customerRepository.save(customer1);

        customer2 = new Customer();
        customer2.setUsername("jane_doe");
        customer2.setFullName("Jane Doe");
        customer2.setEmail("jane.doe@example.com");
        customer2.setPassword("password456");
        customerRepository.save(customer2);
    }

    @AfterAll
    public void cleanUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testGetRecommendationsForCustomer() throws Exception {
        rentalService.rentVideo(customer1.getId(), video1.getId());
        rentalService.rentVideo(customer1.getId(), video2.getId());

        CompletableFuture<List<Video>> recommendations = recommendationService.getRecommendationsForCustomer(customer1.getId());
        recommendations.join();

        assertThat(recommendations.get()).contains(video4);
    }

    @Test
    public void testRecommendationsWithNoRentalHistory() throws Exception {
        List<Video> recommendations = recommendationService.getRecommendationsForCustomer(customer2.getId()).get();

        assertThat(recommendations).isEmpty();
    }

    @Test
    public void testGetSimilarVideos() {
        video2.setGenre("Sci-Fi");
        video2.setDirector("Christopher Nolan");
        videoRepository.save(video2);

        List<Video> similarVideos = recommendationService.findSimilarVideos(video1, videoRepository.findAll());

        assertThat(similarVideos).contains(video2);
    }

    @Test
    public void testRecommendationsWhenNoSimilarVideos() throws Exception {
        rentalService.rentVideo(customer1.getId(), video1.getId());

        video2.setGenre("Comedy");
        video2.setDirector("Some Director");
        videoRepository.save(video2);

        List<Video> recommendations = recommendationService.getRecommendationsForCustomer(customer1.getId()).get();

        assertThat(recommendations).doesNotContain(video2);
    }
}

