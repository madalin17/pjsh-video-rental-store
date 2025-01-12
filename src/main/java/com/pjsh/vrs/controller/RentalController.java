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
    public ResponseEntity<?> rentVideo(@RequestParam Long customerId, @RequestParam Long videoId) {
        try {
            Rental rental = rentalService.rentVideo(customerId, videoId);
            return new ResponseEntity<>(rental, HttpStatus.CREATED);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Return a video
    @PostMapping("/return/{rentalId}")
    public ResponseEntity<?> returnVideo(@PathVariable Long rentalId) {
        try {
            Rental rental = rentalService.returnVideo(rentalId);
            return new ResponseEntity<>(rental, HttpStatus.OK);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // View rental history for a customer
    @GetMapping("/history/{customerId}")
    public ResponseEntity<List<Rental>> getRentalHistory(@PathVariable Long customerId) {
        List<Rental> rentalHistory = rentalService.getRentalHistory(customerId);
        return new ResponseEntity<>(rentalHistory, HttpStatus.OK);
    }
}
