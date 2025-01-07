package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.entity.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Rental rental;
    private Customer customer;
    private Video video;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setUsername("john_doe");
        customer.setEmail("john.doe@example.com");
        customerRepository.save(customer);

        video = new Video();
        video.setTitle("Inception");
        video.setDirector("Christopher Nolan");
        video.setYear(2010);
        video.setQuantity(10);
        videoRepository.save(video);

        rental = new Rental();
        rental.setCustomer(customer);
        rental.setVideo(video);
        rental.setRentalDate(LocalDate.now());
        rental.setStatus(RentalStatus.RENTED);
        rentalRepository.save(rental);
    }

    @Test
    public void testFindByCustomer() {
        // Retrieve rental by customer
        Rental foundRental = rentalRepository.findByCustomer(customer).get(0);

        // Verify result
        assertThat(foundRental.getCustomer()).isEqualTo(customer);
        assertThat(foundRental.getStatus()).isEqualTo(RentalStatus.RENTED);
    }

    @Test
    public void testFindByVideo() {
        // Retrieve rental by video
        Rental foundRental = rentalRepository.findByVideo(video).get(0);

        // Verify result
        assertThat(foundRental.getVideo()).isEqualTo(video);
        assertThat(foundRental.getStatus()).isEqualTo(RentalStatus.RENTED);
    }

    @Test
    public void testFindByStatus() {
        // Retrieve rental by status
        Rental foundRental = rentalRepository.findByStatus(RentalStatus.RENTED).get(0);

        // Verify result
        assertThat(foundRental.getStatus()).isEqualTo(RentalStatus.RENTED);
    }
}
