package com.pjsh.vrs.mock.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.RentalService;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RentalRepository;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RentalService rentalService;

    @Autowired
    private Video video1;

    @Autowired
    private Customer customer1;

    private Rental rental1;

    private Long video1Id, customer1Id;

    @Value("${video1.quantity}")
    private int video1Quantity;

    @BeforeEach
    public void setUp() {
        video1Id = 1L;
        customer1Id = 1L;

        rental1 = new Rental();
        rental1.setVideo(video1);
        rental1.setCustomer(customer1);
        rental1.setRentalDate(LocalDate.now());
        rental1.setStatus(RentalStatus.RENTED);
    }

    @Test
    public void testRentVideo() {
        video1.setQuantity(video1Quantity);
        when(videoRepository.findById(video1Id)).thenReturn(Optional.of(video1));
        when(customerRepository.findById(customer1Id)).thenReturn(Optional.of(customer1));
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental1);

        Rental rented = rentalService.rentVideo(customer1Id, video1Id);

        assertThat(rented).isNotNull();
        assertThat(rented.getStatus()).isEqualTo(RentalStatus.RENTED);
        assertThat(video1.getQuantity()).isEqualTo(video1Quantity - 1);
    }

    @Test
    public void testReturnVideo() {
        video1.setQuantity(video1Quantity);
        rental1.setStatus(RentalStatus.RENTED);
        when(rentalRepository.findById(rental1.getId())).thenReturn(Optional.of(rental1));
        when(videoRepository.save(video1)).thenReturn(video1);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental1);

        Rental returnedRental = rentalService.returnVideo(rental1.getId());

        assertThat(returnedRental.getStatus()).isEqualTo(RentalStatus.RETURNED);
        assertThat(video1.getQuantity()).isEqualTo(video1Quantity + 1);
    }

    @Test
    public void testGetRentalHistory() {
        when(customerRepository.findById(customer1Id)).thenReturn(Optional.of(customer1));
        when(rentalRepository.findByCustomer(customer1)).thenReturn(List.of(rental1));

        List<Rental> rentals = rentalService.getRentalHistory(customer1Id);

        assertThat(rentals).hasSize(1);
        assertThat(rentals.get(0).getStatus()).isEqualTo(RentalStatus.RENTED);
    }

    @Test
    public void testRentVideoOutOfStock() {
        video1.setQuantity(0);
        when(videoRepository.findById(video1Id)).thenReturn(Optional.of(video1));

        try {
            rentalService.rentVideo(customer1Id, video1Id);
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("Video is out of stock");
        }

        verify(rentalRepository, never()).save(any());
    }

    @Test
    public void testReturnAlreadyReturnedVideo() {
        rental1.setStatus(RentalStatus.RETURNED);
        when(rentalRepository.findById(rental1.getId())).thenReturn(Optional.of(rental1));

        try {
            rentalService.returnVideo(rental1.getId());
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("Video already returned");
        }

        verify(rentalRepository, never()).save(any());
    }
}
