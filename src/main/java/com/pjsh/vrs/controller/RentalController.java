package com.pjsh.vrs.controller;

import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    // Rent a video
    @PostMapping("/rent")
    public ResponseEntity<Rental> rentVideo(@RequestParam Long customerId, @RequestParam Long videoId) {
        Rental rental = rentalService.rentVideo(customerId, videoId);
        return new ResponseEntity<>(rental, HttpStatus.CREATED);
    }

    // Return a video
    @PostMapping("/return/{rentalId}")
    public ResponseEntity<Rental> returnVideo(@PathVariable Long rentalId) {
        Rental rental = rentalService.returnVideo(rentalId);
        return new ResponseEntity<>(rental, HttpStatus.OK);
    }

    // View rental history for a customer
    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<Rental>> getRentalHistory(@PathVariable Long customerId) {
        List<Rental> rentalHistory = rentalService.getRentalHistory(customerId);
        return new ResponseEntity<>(rentalHistory, HttpStatus.OK);
    }
}
