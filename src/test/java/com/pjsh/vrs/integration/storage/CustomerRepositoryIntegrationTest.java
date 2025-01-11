package com.pjsh.vrs.integration.storage;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.storage.CustomerRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1, customer2;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();

        customer1 = new Customer();
        customer1.setUsername("john_doe");
        customer1.setFullName("John Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setPassword("password123");
        customerRepository.save(customer1);

        customer2 = new Customer();
        customer2.setUsername("jane_doe");
        customer2.setFullName("Jane Doe");
        customer2.setEmail("jane.doe@example.com");
        customer2.setPassword("password456");
        customerRepository.save(customer2);
    }

    @AfterAll
    public void cleanUp() {
        customerRepository.deleteAll();
    }

    @Test
    public void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setUsername("alice_malice");
        customer.setFullName("Alice Johnson");
        customer.setEmail("alice.johnson@example.com");
        customer.setPassword("555555555");

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer.getId());
        assertEquals("Alice Johnson", savedCustomer.getFullName());
        assertEquals("alice.johnson@example.com", savedCustomer.getEmail());
    }

    @Test
    public void testFindByEmail() {
        Customer foundCustomer = customerRepository.findByEmail("john.doe@example.com");

        assertNotNull(foundCustomer);
        assertEquals("John Doe", foundCustomer.getFullName());
        assertEquals("john.doe@example.com", foundCustomer.getEmail());
    }

    @Test
    public void testFindByEmailNotFound() {
        Customer foundCustomer = customerRepository.findByEmail("non.existent@example.com");

        assertNull(foundCustomer);
    }

    @Test
    public void testDeleteCustomer() {
        customerRepository.delete(customer1);

        Customer deletedCustomer = customerRepository.findByEmail("john.doe@example.com");

        assertNull(deletedCustomer);
    }

    @Test
    public void testCustomerPersistence() {
        customerRepository.save(customer1);

        Customer persistedCustomer = customerRepository.findByEmail("john.doe@example.com");

        assertNotNull(persistedCustomer);
        assertEquals(customer1.getFullName(), persistedCustomer.getFullName());
        assertEquals(customer1.getEmail(), persistedCustomer.getEmail());
    }
}
