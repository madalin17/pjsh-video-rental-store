package com.pjsh.vrs.controller;

import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RentalControllerTest {

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private RentalController rentalController;

    private MockMvc mockMvc;

    private Rental rental;

    @BeforeEach
    public void setUp() {
        rental = new Rental();
        rental.setId(1L);
        rental.setStatus(RentalStatus.RENTED);

        mockMvc = MockMvcBuilders.standaloneSetup(rentalController).build();
    }

    @Test
    public void testRentVideo() throws Exception {
        // Mock service call
        when(rentalService.rentVideo(1L, 1L)).thenReturn(rental);

        // Perform POST request and verify response
        mockMvc.perform(post("/rentals/rent")
                        .param("customerId", "1")
                        .param("videoId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("RENTED"));
    }

    @Test
    public void testReturnVideo() throws Exception {
        // Mock service call
        when(rentalService.returnVideo(1L)).thenReturn(rental);

        // Perform POST request and verify response
        mockMvc.perform(post("/rentals/return/{rentalId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RETURNED"));
    }

    @Test
    public void testGetRentalHistory() throws Exception {
        // Mock service call
        when(rentalService.getRentalHistory(1L)).thenReturn(List.of(rental));

        // Perform GET request and verify response
        mockMvc.perform(get("/rentals/history/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("RENTED"));
    }
}
