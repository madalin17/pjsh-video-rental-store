package com.pjsh.vrs.integration.storage;

import com.pjsh.vrs.entity.Customer;
import com.pjsh.vrs.storage.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TestPropertySource("classpath:test.properties")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Customer customer1, customer2;

    private Customer testCustomer1, testCustomer2;

    @Value("${customer1.fullName}")
    private String customer1FullName;
    @Value("${customer1.email}")
    private String customer1Email;
    @Value("${noCustomer.email}")
    private String noCustomerEmail;

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
    void testSaveCustomer() {
        assertNotNull(testCustomer1.getId());
        assertEquals(customer1FullName, testCustomer1.getFullName());
        assertEquals(customer1Email, testCustomer1.getEmail());
    }

    @Test
    void testFindByEmail() {
        Customer foundCustomer = customerRepository.findByEmail(customer1Email);

        assertNotNull(foundCustomer);
        assertEquals(customer1FullName, foundCustomer.getFullName());
        assertEquals(customer1Email, foundCustomer.getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        Customer foundCustomer = customerRepository.findByEmail(noCustomerEmail);

        assertNull(foundCustomer);
    }

    @Test
    void testDeleteCustomer() {
        customerRepository.delete(testCustomer1);

        Customer deletedCustomer = customerRepository.findByEmail(customer1Email);

        assertNull(deletedCustomer);
    }

    @Test
    void testCustomerPersistence() {
        customerRepository.save(testCustomer1);

        Customer persistedCustomer = customerRepository.findByEmail(customer1Email);

        assertNotNull(persistedCustomer);
        assertEquals(customer1.getFullName(), persistedCustomer.getFullName());
        assertEquals(customer1.getEmail(), persistedCustomer.getEmail());
    }
}
