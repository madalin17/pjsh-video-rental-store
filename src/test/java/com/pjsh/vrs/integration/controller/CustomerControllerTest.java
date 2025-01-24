package com.pjsh.vrs.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.storage.CustomerRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Customer customer1, customer2, customer3;

    private Customer testCustomer1, testCustomer2;

    @Value("${customer1.email}")
    private String customer1Email;
    @Value("${customer1.fullName}")
    private String customer1FullName;
    @Value("${customer2.fullName}")
    private String customer2FullName;
    @Value("${customer3.fullName}")
    private String customer3FullName;
    @Value("${customer3.email}")
    private String customer3Email;


    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();

        testCustomer1 = customerRepository.save(new Customer(customer1));
        testCustomer2 = customerRepository.save(new Customer(customer2));
    }

    @AfterAll
    void cleanUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fullName").value(customer1FullName))
                .andExpect(jsonPath("$[1].fullName").value(customer2FullName));
    }

    @Test
    void testGetCustomerById() throws Exception {
        mockMvc.perform(get("/customers/{id}", testCustomer1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(customer1FullName))
                .andExpect(jsonPath("$.email").value(customer1Email));
    }

    @Test
    void testGetCustomerByEmail() throws Exception {
        mockMvc.perform(get("/customers/email").param("email", customer1Email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(customer1FullName))
                .andExpect(jsonPath("$.email").value(customer1Email));
    }

    @Test
    void testRegisterCustomer() throws Exception {
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer3)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(customer3Email))
                .andExpect(jsonPath("$.fullName").value(customer3FullName));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/{id}", testCustomer1.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
