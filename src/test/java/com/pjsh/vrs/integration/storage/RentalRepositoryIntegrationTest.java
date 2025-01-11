package com.pjsh.vrs.integration.storage;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.entity.Video;
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
public class RentalRepositoryIntegrationTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1, customer2;
    private Video video1, video2;
    private Rental rental1, rental2, rental3;

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

        rental1 = new Rental();
        rental1.setCustomer(customer1);
        rental1.setVideo(video1);
        rental1.setStatus(RentalStatus.RENTED);
        rentalRepository.save(rental1);

        rental2 = new Rental();
        rental2.setCustomer(customer1);
        rental2.setVideo(video2);
        rental2.setStatus(RentalStatus.RETURNED);
        rentalRepository.save(rental2);

        rental3 = new Rental();
        rental3.setCustomer(customer2);
        rental3.setVideo(video1);
        rental3.setStatus(RentalStatus.RENTED);
        rentalRepository.save(rental3);
    }

    @AfterAll
    public void cleanUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testFindByCustomer() {
        List<Rental> rentals = rentalRepository.findByCustomer(customer1);

        assertNotNull(rentals);
        assertEquals(2, rentals.size());
        assertTrue(rentals.stream().anyMatch(r -> r.getVideo().equals(video1)));
        assertTrue(rentals.stream().anyMatch(r -> r.getVideo().equals(video2)));
    }

    @Test
    public void testFindByVideo() {
        List<Rental> rentals = rentalRepository.findByVideo(video1);

        assertNotNull(rentals);
        assertEquals(2, rentals.size());
        assertTrue(rentals.stream().anyMatch(r -> r.getCustomer().equals(customer1)));
        assertTrue(rentals.stream().anyMatch(r -> r.getCustomer().equals(customer2)));
    }

    @Test
    public void testFindByStatus() {
        List<Rental> rented = rentalRepository.findByStatus(RentalStatus.RENTED);

        assertNotNull(rented);
        assertEquals(2, rented.size());
        assertTrue(rented.stream().anyMatch(r -> r.getVideo().equals(video1)));
        assertTrue(rented.stream().anyMatch(r -> r.getCustomer().equals(customer2)));

        List<Rental> returned = rentalRepository.findByStatus(RentalStatus.RETURNED);

        assertNotNull(returned);
        assertEquals(1, returned.size());
        assertEquals(video2, returned.get(0).getVideo());
        assertEquals(customer1, returned.get(0).getCustomer());
    }
}
