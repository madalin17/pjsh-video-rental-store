package com.pjsh.vrs.mock.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.service.CustomerService;
import com.pjsh.vrs.storage.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setUsername("john_doe");
        customer.setFullName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password123");
    }

    @Test
    public void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        List<Customer> customers = customerService.getAllCustomers();
        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertEquals("john_doe", customers.get(0).getUsername());
    }

    @Test
    public void testGetCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> customer = customerService.getCustomerById(1L);
        assertTrue(customer.isPresent());
        assertEquals("john_doe", customer.get().getUsername());
    }

    @Test
    public void testGetCustomerByEmail() {
        when(customerRepository.findByEmail("john.doe@example.com")).thenReturn(customer);

        Customer result = customerService.getCustomerByEmail("john.doe@example.com");
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
    }

    @Test
    public void testRegisterCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.registerCustomer(customer);
        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
    }

    @Test
    public void testDeleteCustomer() {
        doNothing().when(customerRepository).deleteById(1L);

        customerService.deleteCustomer(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }
}
