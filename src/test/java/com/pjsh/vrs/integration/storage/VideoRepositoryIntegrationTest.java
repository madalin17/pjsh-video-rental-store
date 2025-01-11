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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VideoRepositoryIntegrationTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Video video1, video2, video3;
    private Customer customer;

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

        customer = new Customer();
        customer.setUsername("john_doe");
        customer.setFullName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password123");
        customerRepository.save(customer);

        Rental rental = new Rental();
        rental.setCustomer(customer);
        rental.setVideo(video1);
        rental.setStatus(RentalStatus.RENTED);
        rentalRepository.save(rental);

        rental = new Rental();
        rental.setCustomer(customer);
        rental.setVideo(video3);
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
        List<Video> videos = videoRepository.findByTitleContainingIgnoreCase("inception");

        assertNotNull(videos);
        assertEquals(1, videos.size());
        assertEquals("Inception", videos.get(0).getTitle());
    }

    @Test
    public void testFindByGenre() {
        List<Video> videos = videoRepository.findByGenre("Sci-Fi");

        assertNotNull(videos);
        assertEquals(1, videos.size());
        assertEquals("Inception", videos.get(0).getTitle());
    }

    @Test
    public void testFindRentedByCustomerId() {
        List<Video> rentedVideos = videoRepository.findRentedByCustomerId(customer.getId());

        assertNotNull(rentedVideos);
        assertEquals(2, rentedVideos.size());
        assertTrue(rentedVideos.stream().anyMatch(v -> v.getTitle().equals("Inception")));
        assertTrue(rentedVideos.stream().anyMatch(v -> v.getTitle().equals("The Dark Knight")));
    }
}
