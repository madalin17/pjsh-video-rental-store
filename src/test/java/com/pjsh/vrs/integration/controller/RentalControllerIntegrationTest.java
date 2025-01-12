package com.pjsh.vrs.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.entity.Rental;
import com.pjsh.vrs.entity.RentalStatus;
import com.pjsh.vrs.entity.Video;
import com.pjsh.vrs.service.RentalService;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.storage.RentalRepository;
import com.pjsh.vrs.storage.VideoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class RentalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Video video1;

    @Autowired
    private Customer customer1;

    private Video testVideo1;

    private Customer testCustomer1;

    private Rental testRental1;

    @BeforeEach
    public void setUp() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();

        testVideo1 = videoRepository.save(new Video(video1));

        testCustomer1 = customerRepository.save(new Customer(customer1));

        testRental1 = rentalService.rentVideo(testCustomer1.getId(), testVideo1.getId());
    }

    @AfterEach
    public void tearDown() {
        rentalRepository.deleteAll();
        videoRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void testRentVideo() throws Exception {
        mockMvc.perform(post("/rentals/rent")
                        .param("customerId", testCustomer1.getId().toString())
                        .param("videoId", testVideo1.getId().toString()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customer.username").value(testCustomer1.getUsername()))
                .andExpect(jsonPath("$.video.title").value(testVideo1.getTitle()))
                .andExpect(jsonPath("$.returnDate").isNotEmpty());
    }

    @Test
    public void testReturnVideo() throws Exception {
        mockMvc.perform(post("/rentals/return/{rentalId}", testRental1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.username").value(testCustomer1.getUsername()))
                .andExpect(jsonPath("$.video.title").value(testVideo1.getTitle()))
                .andExpect(jsonPath("$.returnDate").isNotEmpty());
    }

    @Test
    public void testGetRentalHistory() throws Exception {
        mockMvc.perform(get("/rentals/history/{customerId}", testCustomer1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customer.username").value(testCustomer1.getUsername()))
                .andExpect(jsonPath("$[0].video.title").value(testVideo1.getTitle()))
                .andExpect(jsonPath("$[0].returnDate").isNotEmpty());
    }

    @Test
    public void testRentUnavailableVideo() throws Exception {
        testVideo1.setQuantity(0);
        videoRepository.save(testVideo1);

        mockMvc.perform(post("/rentals/rent")
                        .param("customerId", testCustomer1.getId().toString())
                        .param("videoId", testVideo1.getId().toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Video is out of stock"));
    }
}
