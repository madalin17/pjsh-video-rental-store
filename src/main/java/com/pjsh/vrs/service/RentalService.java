package com.pjsh.vrs.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RentalRepository;
import com.pjsh.vrs.storage.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Rental rentVideo(Long customerId, Long videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new EntityNotFoundException("Video not found"));
        if (!video.isAvailable()) {
            throw new IllegalStateException("Video is out of stock");
        }

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Rental rental = new Rental();
        rental.setCustomer(customer);
        rental.setVideo(video);
        rental.setRentalDate(LocalDate.now());
        rental.setReturnDate(LocalDate.now().plusDays(14));
        rental.setStatus(RentalStatus.RENTED);

        video.setQuantity(video.getQuantity() - 1);
        videoRepository.save(video);

        return rentalRepository.save(rental);
    }

    public Rental returnVideo(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        if (rental.getStatus() == RentalStatus.RETURNED) {
            throw new IllegalStateException("Video already returned");
        }

        rental.setStatus(RentalStatus.RETURNED);
        rental.setReturnDate(LocalDate.now());

        Video video = rental.getVideo();
        video.setQuantity(video.getQuantity() + 1);
        videoRepository.save(video);

        return rentalRepository.save(rental);
    }

    public List<Rental> getRentalHistory(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return rentalRepository.findByCustomer(customer);
    }
}

