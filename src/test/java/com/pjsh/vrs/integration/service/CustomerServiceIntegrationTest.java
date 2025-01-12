package com.pjsh.vrs.integration.service;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.storage.CustomerRepository;
import com.pjsh.vrs.service.CustomerService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
public class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Customer customer1, customer2;

    private Customer testCustomer1, testCustomer2;

    @Value("${customer1.username}")
    private String customer1Username;
    @Value("${customer1.email}")
    private String customer1Email;
    @Value("${customer2.fullName}")
    private String customer2FullName;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();

        testCustomer1 = customerRepository.save(new Customer(customer1));
        testCustomer2 = customerRepository.save(new Customer(customer2));
    }

    @AfterAll
    public void cleanUp() {
        customerRepository.deleteAll();
    }

    @Test
    public void testCreateCustomer() {
        assertThat(testCustomer1).isNotNull();
        assertThat(testCustomer1.getId()).isNotNull();
        assertThat(testCustomer1.getUsername()).isEqualTo(customer1Username);

        customerRepository.delete(testCustomer1);
    }

    @Test
    public void testGetCustomerById() {
        Optional<Customer> fetchedCustomer = customerService.getCustomerById(testCustomer1.getId());

        assertThat(fetchedCustomer).isPresent();
        assertThat(fetchedCustomer.get().getUsername()).isEqualTo(customer1Username);
    }

    @Test
    public void testGetCustomerByEmail() {
        Customer fetchedCustomer = customerService.getCustomerByEmail(customer1Email);

        assertThat(fetchedCustomer).isNotNull();
        assertThat(fetchedCustomer.getEmail()).isEqualTo(customer1Email);

        customerRepository.delete(fetchedCustomer);
    }

    @Test
    public void testUpdateCustomer() {
        testCustomer1.setFullName(customer2FullName);

        Customer updatedCustomer = customerService.registerCustomer(testCustomer1);

        assertThat(updatedCustomer.getFullName()).isEqualTo(customer2FullName);

        customerRepository.delete(updatedCustomer);
    }

    @Test
    public void testDeleteCustomer() {
        customerService.deleteCustomer(testCustomer1.getId());

        Optional<Customer> deletedCustomer = customerRepository.findById(testCustomer1.getId());
        assertThat(deletedCustomer).isEmpty();
    }

    @Test
    public void testGetAllCustomers() {
        Iterable<Customer> customers = customerService.getAllCustomers();

        assertThat(customers).hasSize(2);
    }
}

