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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Autowired
    private Customer customer1, customer2;

    private Long customer1Id, customer2Id;

    @BeforeEach
    void setUp() {
        customer1Id = 1L;
        customer2Id = 2L;
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<Customer> customers = customerService.getAllCustomers();
        assertNotNull(customers);
        assertEquals(2, customers.size());
        assertEquals(customer1.getUsername(), customers.get(0).getUsername());
        assertEquals(customer2.getUsername(), customers.get(1).getUsername());
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(customer1Id)).thenReturn(Optional.of(customer1));

        Optional<Customer> customer = customerService.getCustomerById(customer1Id);
        assertTrue(customer.isPresent());
        assertEquals(customer1.getUsername(), customer.get().getUsername());
    }

    @Test
    void testGetCustomerByEmail() {
        when(customerRepository.findByEmail(customer1.getEmail())).thenReturn(customer1);

        Customer result = customerService.getCustomerByEmail(customer1.getEmail());
        assertNotNull(result);
        assertEquals(customer1.getUsername(), result.getUsername());
    }

    @Test
    void testRegisterCustomer() {
        when(customerRepository.save(customer2)).thenReturn(customer2);

        Customer result = customerService.registerCustomer(customer2);
        assertNotNull(result);
        assertEquals(customer2.getUsername(), result.getUsername());
    }

    @Test
    void testDeleteCustomer() {
        doNothing().when(customerRepository).deleteById(customer1Id);

        customerService.deleteCustomer(customer1Id);
        verify(customerRepository, times(1)).deleteById(customer1Id);
    }
}
