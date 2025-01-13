package com.pjsh.vrs.mock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.controller.RentalController;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.service.RentalService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RentalControllerTest {

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private RentalController rentalController;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Video video1, video2;

    @Autowired
    private Customer customer1, customer2;

    private Rental rental1, rental2, rental3;

    private Long video1Id, video2Id, customer1Id, customer2Id, rental1Id, rental2Id;

    @Value("${video1.title}")
    private String video1Title;
    @Value("${video2.title}")
    private String video2Title;
    @Value("${customer1.username}")
    private String customer1Username;
    @Value("${customer2.username}")
    private String customer2Username;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(rentalController).build();

        video1Id = 1L;
        video2Id = 2L;
        customer1Id = 1L;
        customer2Id = 2L;
        rental1Id = 1L;
        rental2Id = 2L;

        rental1 = new Rental();
        rental1.setVideo(video1);
        rental1.setCustomer(customer1);
        rental1.setStatus(RentalStatus.RENTED);

        rental2 = new Rental();
        rental2.setVideo(video2);
        rental2.setCustomer(customer1);
        rental2.setStatus(RentalStatus.RENTED);

        rental3 = new Rental();
        rental3.setVideo(video2);
        rental3.setCustomer(customer2);
        rental3.setStatus(RentalStatus.RENTED);

        mockMvc.perform(post("/rentals/rent")
                        .param("customerId", String.valueOf(customer1Id))
                        .param("videoId", String.valueOf(video1Id)))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(post("/rentals/rent")
                        .param("customerId", String.valueOf(customer1Id))
                        .param("videoId", String.valueOf(video2Id)))
                .andDo(print())
                .andExpect(status().isCreated());

        when(rentalService.rentVideo(customer1Id, video1Id)).thenReturn(rental1);
        when(rentalService.rentVideo(customer1Id, video2Id)).thenReturn(rental2);
        when(rentalService.getRentalHistory(customer1Id)).thenReturn(List.of(rental1, rental2));
        when(rentalService.getRentalHistory(customer2Id)).thenReturn(List.of());
    }

    @Test
    public void testRentVideo() throws Exception {
        when(rentalService.rentVideo(customer2Id, video2Id)).thenReturn(rental3);

        mockMvc.perform(post("/rentals/rent")
                        .param("customerId", String.valueOf(customer2Id))
                        .param("videoId", String.valueOf(video2Id)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customer.username").value(customer2Username))
                .andExpect(jsonPath("$.video.title").value(video2Title));
    }

    @Test
    public void testReturnVideo() throws Exception {
        mockMvc.perform(post("/rentals/return/{rentalId}", rental1Id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRentalHistory() throws Exception {
        mockMvc.perform(get("/rentals/history/{customerId}", customer1Id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].video.title").value(video1Title))
                .andExpect(jsonPath("$[1].video.title").value(video2Title));
    }

    @Test
    public void testReturnVideoWithInvalidRentalId() throws Exception {
        when(rentalService.returnVideo(999L)).thenThrow(new IllegalStateException("Rental ID not found"));

        mockMvc.perform(post("/rentals/return/{rentalId}", 999L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Rental ID not found"));
    }

    @Test
    public void testGetRentalHistoryForNonExistentCustomer() throws Exception {
        mockMvc.perform(get("/rentals/history/{customerId}", 999L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
