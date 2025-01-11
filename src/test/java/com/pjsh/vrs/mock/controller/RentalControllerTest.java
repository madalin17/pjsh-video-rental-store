package com.pjsh.vrs.mock.controller;

import com.pjsh.vrs.controller.RentalController;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.service.RentalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RentalControllerTest {

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private RentalController rentalController;

    private MockMvc mockMvc;

    private Rental rented;

    private Rental returned;

    @BeforeEach
    public void setUp() {
        rented = new Rental();
        rented.setId(1L);
        rented.setStatus(RentalStatus.RENTED);

        returned = new Rental();
        returned.setId(1L);
        returned.setStatus(RentalStatus.RETURNED);

        mockMvc = MockMvcBuilders.standaloneSetup(rentalController).build();
    }

    @Test
    public void testRentVideo() throws Exception {
        when(rentalService.rentVideo(1L, 1L)).thenReturn(rented);

        mockMvc.perform(post("/rentals/rent")
                        .param("customerId", "1")
                        .param("videoId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("RENTED"));
    }

    @Test
    public void testReturnVideo() throws Exception {
        when(rentalService.returnVideo(1L)).thenReturn(returned);

        mockMvc.perform(post("/rentals/return/{rentalId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RETURNED"));
    }

    @Test
    public void testGetRentalHistory() throws Exception {
        when(rentalService.getRentalHistory(1L)).thenReturn(List.of(rented));

        mockMvc.perform(get("/rentals/history/{customerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("RENTED"));
    }
}
