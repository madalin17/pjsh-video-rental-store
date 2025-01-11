package com.pjsh.vrs.mock.controller;

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
import org.springframework.boot.test.context.SpringBootTest;
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
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    private Customer customer1, customer2;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setUsername("john_doe");
        customer1.setFullName("John Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setPassword("password123");

        customer2 = new Customer();
        customer2.setId(2L);
        customer2.setUsername("jane_doe");
        customer2.setFullName("Jane Doe");
        customer2.setEmail("jane.doe@example.com");
        customer2.setPassword("password123");
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        List<Customer> customerList = Arrays.asList(customer1, customer2);
        when(customerService.getAllCustomers()).thenReturn(customerList);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(java.util.Optional.of(customer1));

        mockMvc.perform(get("/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    @Test
    public void testGetCustomerByEmail() throws Exception {
        when(customerService.getCustomerByEmail("john.doe@example.com")).thenReturn(customer1);

        mockMvc.perform(get("/customers/email")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    @Test
    public void testRegisterCustomer() throws Exception {
        when(customerService.registerCustomer(customer1)).thenReturn(customer1);

        mockMvc.perform(post("/customers")
                        .contentType("application/json")
                        .content("{ \"username\": \"john_doe\", \"fullName\": \"John Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password123\" }"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customers/{id}", 1L))
                .andExpect(status().isOk());
    }
}
