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
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Video video1, video2;

    @Autowired
    private Customer customer1, customer2;

    private Video testVideo1, testVideo2;

    private Customer testCustomer1, testCustomer2;

    private Rental testRental1, testRetal2, testRental3;

    @BeforeEach
    public void setUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));
        testVideo2 = videoRepository.save(new Video(video2));

        testCustomer1 = customerRepository.save(new Customer(customer1));
        testCustomer2 = customerRepository.save(new Customer(customer2));

        testRental1 = new Rental();
        testRental1.setCustomer(testCustomer1);
        testRental1.setVideo(testVideo1);
        testRental1.setStatus(RentalStatus.RENTED);
        rentalRepository.save(testRental1);

        testRetal2 = new Rental();
        testRetal2.setCustomer(testCustomer1);
        testRetal2.setVideo(testVideo2);
        testRetal2.setStatus(RentalStatus.RETURNED);
        rentalRepository.save(testRetal2);

        testRental3 = new Rental();
        testRental3.setCustomer(testCustomer2);
        testRental3.setVideo(testVideo1);
        testRental3.setStatus(RentalStatus.RENTED);
        rentalRepository.save(testRental3);
    }

    @AfterAll
    public void cleanUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testFindByCustomer() {
        List<Rental> rentals = rentalRepository.findByCustomer(testCustomer1);

        assertNotNull(rentals);
        assertEquals(2, rentals.size());
        assertTrue(rentals.stream().anyMatch(r -> r.getVideo().equals(testVideo1)));
        assertTrue(rentals.stream().anyMatch(r -> r.getVideo().equals(testVideo1)));
    }

    @Test
    public void testFindByVideo() {
        List<Rental> rentals = rentalRepository.findByVideo(testVideo1);

        assertNotNull(rentals);
        assertEquals(2, rentals.size());
        assertTrue(rentals.stream().anyMatch(r -> r.getCustomer().equals(testCustomer1)));
        assertTrue(rentals.stream().anyMatch(r -> r.getCustomer().equals(testCustomer2)));
    }

    @Test
    public void testFindByStatus() {
        List<Rental> rented = rentalRepository.findByStatus(RentalStatus.RENTED);

        assertNotNull(rented);
        assertEquals(2, rented.size());
        assertTrue(rented.stream().anyMatch(r -> r.getVideo().equals(testVideo1)));
        assertTrue(rented.stream().anyMatch(r -> r.getCustomer().equals(testCustomer2)));

        List<Rental> returned = rentalRepository.findByStatus(RentalStatus.RETURNED);

        assertNotNull(returned);
        assertEquals(1, returned.size());
        assertEquals(testVideo2, returned.get(0).getVideo());
        assertEquals(testCustomer1, returned.get(0).getCustomer());
    }
}
