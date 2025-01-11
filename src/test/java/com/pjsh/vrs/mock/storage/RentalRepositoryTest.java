package com.pjsh.vrs.mock.storage;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RentalRepository;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

        rental = new Rental();
        rental.setCustomer(customer);
        rental.setVideo(video);
        rental.setRentalDate(LocalDate.now());
        rental.setStatus(RentalStatus.RENTED);
        rentalRepository.save(rental);
    }

    @Test
    public void testFindByCustomer() {
        Rental foundRental = rentalRepository.findByCustomer(customer).get(0);

        assertThat(foundRental.getCustomer()).isEqualTo(customer);
        assertThat(foundRental.getStatus()).isEqualTo(RentalStatus.RENTED);
    }

    @Test
    public void testFindByVideo() {
        Rental foundRental = rentalRepository.findByVideo(video).get(0);

        assertThat(foundRental.getVideo()).isEqualTo(video);
        assertThat(foundRental.getStatus()).isEqualTo(RentalStatus.RENTED);
    }

    @Test
    public void testFindByStatus() {
        Rental foundRental = rentalRepository.findByStatus(RentalStatus.RENTED).get(0);

        assertThat(foundRental.getStatus()).isEqualTo(RentalStatus.RENTED);
    }
}
