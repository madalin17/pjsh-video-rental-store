package com.pjsh.vrs.mock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjsh.vrs.controller.CustomerController;
import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.service.CustomerService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Customer customer1, customer2;

    private Long customer1Id, customer2Id;

    @Value("${customer1.email}")
    private String customer1Email;
    @Value("${customer1.username}")
    private String customer1Username;
    @Value("${customer2.email}")
    private String customer2Email;
    @Value("${customer2.username}")
    private String customer2Username;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        customer1Id = 1L;
        customer2Id = 2L;
    }

    @Test
    void shouldReturnAllCustomers() throws Exception {
        List<Customer> customerList = Arrays.asList(customer1, customer2);
        when(customerService.getAllCustomers()).thenReturn(customerList);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").value(customer1Email))
                .andExpect(jsonPath("$[1].email").value(customer2Email));
    }

    @Test
    void shouldReturnCustomerById() throws Exception {
        when(customerService.getCustomerById(customer1Id)).thenReturn(java.util.Optional.of(customer1));

        mockMvc.perform(get("/customers/{id}", customer1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(customer1Email))
                .andExpect(jsonPath("$.username").value(customer1Username));
    }

    @Test
    void shouldReturnCustomerByEmail() throws Exception {
        when(customerService.getCustomerByEmail(customer1Email)).thenReturn(customer1);

        mockMvc.perform(get("/customers/email").param("email", customer1Email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(customer1Email))
                .andExpect(jsonPath("$.username").value(customer1Username));
    }

    @Test
    void shouldRegisterCustomer() throws Exception {
        when(customerService.registerCustomer(customer2)).thenReturn(customer2);

        System.out.println(objectMapper.writeValueAsString(customer2));
        mockMvc.perform(post("/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(customer2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(customer2Email))
                .andExpect(jsonPath("$.username").value(customer2Username));
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(customer1Id);

        mockMvc.perform(delete("/customers/{id}", customer1Id))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteCustomer(customer1Id);
    }
}
