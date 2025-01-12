package com.pjsh.vrs.integration.storage;

import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RentalRepository;
import com.pjsh.vrs.storage.VideoRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class VideoRepositoryIntegrationTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Video video1, video2, video3;

    @Autowired
    private Customer customer1;

    private Video testVideo1, testVideo2;

    private Customer testCustomer1;

    @Value("${video1.lowercaseTitle}")
    private String video1LowercaseTitle;
    @Value("${video1.title}")
    private String video1Title;
    @Value("${video1.genre}")
    private String video1Genre;
    @Value("${video2.title}")
    private String video2Title;

    @BeforeEach
    public void setUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));
        testVideo2 = videoRepository.save(new Video(video2));

        testCustomer1 = customerRepository.save(new Customer(customer1));

        Rental rental = new Rental();
        rental.setCustomer(testCustomer1);
        rental.setVideo(testVideo1);
        rental.setStatus(RentalStatus.RENTED);
        rentalRepository.save(rental);

        rental = new Rental();
        rental.setCustomer(testCustomer1);
        rental.setVideo(testVideo2);
        rental.setStatus(RentalStatus.RENTED);
        rentalRepository.save(rental);
    }

    @AfterAll
    public void cleanUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testFindByTitleContainingIgnoreCase() {
        List<Video> videos = videoRepository.findByTitleContainingIgnoreCase(video1LowercaseTitle);

        assertNotNull(videos);
        assertEquals(1, videos.size());
        assertEquals(video1Title, videos.get(0).getTitle());
    }

    @Test
    public void testFindByGenre() {
        List<Video> videos = videoRepository.findByGenre(video1Genre);

        assertNotNull(videos);
        assertEquals(1, videos.size());
        assertEquals(video1Title, videos.get(0).getTitle());
    }

    @Test
    public void testFindRentedByCustomerId() {
        List<Video> rentedVideos = videoRepository.findRentedByCustomerId(testCustomer1.getId());

        assertNotNull(rentedVideos);
        assertEquals(2, rentedVideos.size());
        assertTrue(rentedVideos.stream().anyMatch(v -> v.getTitle().equals(video1Title)));
        assertTrue(rentedVideos.stream().anyMatch(v -> v.getTitle().equals(video2Title)));
    }
}
