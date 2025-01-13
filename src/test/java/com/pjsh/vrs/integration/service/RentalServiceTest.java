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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RentalServiceTest {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private Video video1;

    @Autowired
    private Customer customer1;

    private Video testVideo1;

    private Customer testCustomer1;

    @Value("${video1.title}")
    private String video1Title;

    @BeforeEach
    public void setUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));

        testCustomer1 = customerRepository.save(new Customer(customer1));
    }

    @AfterAll
    public void cleanUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testRentAndReturnVideo() {
        Rental rental = rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId());
        assertThat(rental).isNotNull();
        assertThat(rental.getCustomer().getId()).isEqualTo(testCustomer1.getId());
        assertThat(rental.getVideo().getId()).isEqualTo(testVideo1.getId());
        assertThat(rental.getStatus()).isEqualTo(RentalStatus.RENTED);
        assertThat(testVideo1.getQuantity()).isEqualTo(4);

        Rental returnedRental = rentalService.returnVideo(rental.getId());
        assertThat(returnedRental.getStatus()).isEqualTo(RentalStatus.RETURNED);
        assertThat(testVideo1.getQuantity()).isEqualTo(5);
    }

    @Test
    public void testRentVideoWhenOutOfStock() {
        testVideo1.setQuantity(0);
        videoRepository.save(testVideo1);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId()));
        assertThat(exception.getMessage()).isEqualTo("Video is out of stock");
    }

    @Test
    public void testReturnVideoThatIsAlreadyReturned() {
        Rental rental = rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId());
        rentalService.returnVideo(rental.getId());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> rentalService.returnVideo(rental.getId()));
        assertThat(exception.getMessage()).isEqualTo("Video already returned");
    }

    @Test
    public void testGetRentalHistory() {
        Rental rental = rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId());

        List<Rental> rentals = rentalService.getRentalHistory(testCustomer1.getId());
        assertThat(rentals).hasSize(1);
        assertThat(rentals.get(0).getVideo().getTitle()).isEqualTo(video1Title);
        assertThat(rentals.get(0).getStatus()).isEqualTo(RentalStatus.RENTED);
    }

    @Test
    public void testRentVideoWithNonExistenttestCustomer1() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> rentalService.rentVideo(999L, testVideo1.getId()));
        assertThat(exception.getMessage()).isEqualTo("Customer not found");
    }

    @Test
    public void testRentVideoWithNonExistentVideo() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> rentalService.rentVideo(testCustomer1.getId(), 999L));
        assertThat(exception.getMessage()).isEqualTo("Video not found");
    }

    @Test
    public void testRentVideoReducesStockCorrectly() {
        for (int i = 0; i < 3; i++) {
            rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId());
        }

        assertThat(testVideo1.getQuantity()).isEqualTo(2);
    }

    @Test
    public void testReturnVideoUpdatesRentalAndVideo() {
        Rental rental = rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId());

        Rental returnedRental = rentalService.returnVideo(rental.getId());

        assertThat(returnedRental.getStatus()).isEqualTo(RentalStatus.RETURNED);
        assertThat(returnedRental.getReturnDate()).isEqualTo(LocalDate.now());
        assertThat(testVideo1.getQuantity()).isEqualTo(5);
    }

    @Test
    public void testGetRentalHistoryFortCustomerWithNoRentals() {
        List<Rental> rentals = rentalService.getRentalHistory(testCustomer1.getId());
        assertThat(rentals).isEmpty();
    }
}

