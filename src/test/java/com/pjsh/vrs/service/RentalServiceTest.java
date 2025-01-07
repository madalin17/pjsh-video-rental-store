package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RentalRepository;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RentalService rentalService;

    private Rental rental;
    private Customer customer;
    private Video video;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setId(1L);

        video = new Video();
        video.setId(1L);
        video.setQuantity(5);

        rental = new Rental();
        rental.setId(1L);
        rental.setCustomer(customer);
        rental.setVideo(video);
        rental.setRentalDate(LocalDate.now());
        rental.setStatus(RentalStatus.RENTED);
    }

    @Test
    public void testRentVideo() {
        // Mock repository calls
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        // Call service method
        Rental rented = rentalService.rentVideo(1L, 1L);

        // Verify result
        assertThat(rented).isNotNull();
        assertThat(rented.getStatus()).isEqualTo(RentalStatus.RENTED);
        assertThat(video.getQuantity()).isEqualTo(4);
    }

    @Test
    public void testReturnVideo() {
        // Mock repository calls
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(videoRepository.save(video)).thenReturn(video);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        // Call service method
        Rental returnedRental = rentalService.returnVideo(1L);

        // Verify result
        assertThat(returnedRental.getStatus()).isEqualTo(RentalStatus.RETURNED);
        assertThat(video.getQuantity()).isEqualTo(5);
    }

    @Test
    public void testGetRentalHistory() {
        // Mock repository call
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rentalRepository.findByCustomer(customer)).thenReturn(List.of(rental));

        // Call service method
        List<Rental> rentals = rentalService.getRentalHistory(1L);

        // Verify result
        assertThat(rentals).hasSize(1);
        assertThat(rentals.get(0).getStatus()).isEqualTo(RentalStatus.RENTED);
    }
}
