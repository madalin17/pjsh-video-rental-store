package com.pjsh.vrs.integration.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.service.CustomerService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setUsername("john_doe");
        customer.setFullName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password123");
    }

    @AfterAll
    public void cleanUp() {
        customerRepository.deleteAll();
    }

    @Test
    public void testCreateCustomer() {
        Customer savedCustomer = customerService.registerCustomer(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getUsername()).isEqualTo("john_doe");

        customerRepository.delete(savedCustomer);
    }

    @Test
    public void testGetCustomerById() {
        Customer savedCustomer = customerRepository.save(customer);

        Optional<Customer> fetchedCustomer = customerService.getCustomerById(savedCustomer.getId());

        assertThat(fetchedCustomer).isPresent();
        assertThat(fetchedCustomer.get().getUsername()).isEqualTo("john_doe");

        customerRepository.delete(savedCustomer);
    }

    @Test
    public void testGetCustomerByEmail() {
        customerRepository.save(customer);

        Customer fetchedCustomer = customerService.getCustomerByEmail("john.doe@example.com");

        assertThat(fetchedCustomer).isNotNull();
        assertThat(fetchedCustomer.getEmail()).isEqualTo("john.doe@example.com");

        customerRepository.delete(fetchedCustomer);
    }

    @Test
    public void testUpdateCustomer() {
        Customer savedCustomer = customerRepository.save(customer);
        savedCustomer.setFullName("Johnathan Doe");

        Customer updatedCustomer = customerService.registerCustomer(savedCustomer);

        assertThat(updatedCustomer.getFullName()).isEqualTo("Johnathan Doe");

        customerRepository.delete(updatedCustomer);
    }

    @Test
    public void testDeleteCustomer() {
        Customer savedCustomer = customerRepository.save(customer);

        customerService.deleteCustomer(savedCustomer.getId());

        Optional<Customer> deletedCustomer = customerRepository.findById(savedCustomer.getId());
        assertThat(deletedCustomer).isEmpty();
    }

    @Test
    public void testGetAllCustomers() {
        customerRepository.save(customer);
        Customer anotherCustomer = new Customer();
        anotherCustomer.setUsername("jane_doe");
        anotherCustomer.setFullName("Jane Doe");
        anotherCustomer.setEmail("jane.doe@example.com");
        anotherCustomer.setPassword("password123");
        customerRepository.save(anotherCustomer);

        Iterable<Customer> customers = customerService.getAllCustomers();

        assertThat(customers).hasSize(2);

        customerRepository.delete(customer);
        customerRepository.delete(anotherCustomer);
    }
}

