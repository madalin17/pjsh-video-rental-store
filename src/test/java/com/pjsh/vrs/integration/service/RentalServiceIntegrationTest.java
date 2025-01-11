package com.pjsh.vrs.integration.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.RentalService;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RentalRepository;
import com.pjsh.vrs.storage.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentalServiceIntegrationTest {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private RentalRepository rentalRepository;

    private Customer customer;
    private Video video;

    @BeforeEach
    public void setUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        video = new Video();
        video.setTitle("Inception");
        video.setDirector("Christopher Nolan");
        video.setActors("Leonardo DiCaprio, Joseph Gordon-Levitt");
        video.setYear(2010);
        video.setDuration("148 min");
        video.setGenre("Sci-Fi");
        video.setDescription("A mind-bending thriller about dreams within dreams.");
        video.setQuantity(5);
        videoRepository.save(video);

        customer = new Customer();
        customer.setUsername("john_doe");
        customer.setFullName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password123");
        customerRepository.save(customer);
    }

    @AfterAll
    public void cleanUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testRentAndReturnVideo() {
        Rental rental = rentalService.rentVideo(customer.getId(), video.getId());
        assertThat(rental).isNotNull();
        assertThat(rental.getCustomer().getId()).isEqualTo(customer.getId());
        assertThat(rental.getVideo().getId()).isEqualTo(video.getId());
        assertThat(rental.getStatus()).isEqualTo(RentalStatus.RENTED);
        assertThat(video.getQuantity()).isEqualTo(4);

        Rental returnedRental = rentalService.returnVideo(rental.getId());
        assertThat(returnedRental.getStatus()).isEqualTo(RentalStatus.RETURNED);
        assertThat(video.getQuantity()).isEqualTo(5);
    }

    @Test
    public void testRentVideoWhenOutOfStock() {
        video.setQuantity(0);
        videoRepository.save(video);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> rentalService.rentVideo(customer.getId(), video.getId()));
        assertThat(exception.getMessage()).isEqualTo("Video is out of stock");
    }

    @Test
    public void testReturnVideoThatIsAlreadyReturned() {
        Rental rental = rentalService.rentVideo(customer.getId(), video.getId());
        rentalService.returnVideo(rental.getId());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> rentalService.returnVideo(rental.getId()));
        assertThat(exception.getMessage()).isEqualTo("Video already returned");
    }

    @Test
    public void testGetRentalHistory() {
        Rental rental = rentalService.rentVideo(customer.getId(), video.getId());

        List<Rental> rentals = rentalService.getRentalHistory(customer.getId());
        assertThat(rentals).hasSize(1);
        assertThat(rentals.get(0).getVideo().getTitle()).isEqualTo("Inception");
        assertThat(rentals.get(0).getStatus()).isEqualTo(RentalStatus.RENTED);
    }

    @Test
    public void testRentVideoWithNonExistentCustomer() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> rentalService.rentVideo(999L, video.getId()));
        assertThat(exception.getMessage()).isEqualTo("Customer not found");
    }

    @Test
    public void testRentVideoWithNonExistentVideo() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> rentalService.rentVideo(customer.getId(), 999L));
        assertThat(exception.getMessage()).isEqualTo("Video not found");
    }

    @Test
    public void testRentVideoReducesStockCorrectly() {
        for (int i = 0; i < 3; i++) {
            rentalService.rentVideo(customer.getId(), video.getId());
        }

        assertThat(video.getQuantity()).isEqualTo(2);
    }

    @Test
    public void testReturnVideoUpdatesRentalAndVideo() {
        Rental rental = rentalService.rentVideo(customer.getId(), video.getId());

        Rental returnedRental = rentalService.returnVideo(rental.getId());

        assertThat(returnedRental.getStatus()).isEqualTo(RentalStatus.RETURNED);
        assertThat(returnedRental.getReturnDate()).isEqualTo(LocalDate.now());
        assertThat(video.getQuantity()).isEqualTo(5);
    }

    @Test
    public void testGetRentalHistoryForCustomerWithNoRentals() {
        List<Rental> rentals = rentalService.getRentalHistory(customer.getId());
        assertThat(rentals).isEmpty();
    }
}

