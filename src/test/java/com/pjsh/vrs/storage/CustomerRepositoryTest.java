package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1, customer2;

    @BeforeEach
    public void setUp() {
        customer1 = new Customer();
        customer1.setUsername("john_doe");
        customer1.setFullName("John Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setPassword("password123");

        customer2 = new Customer();
        customer2.setUsername("jane_doe");
        customer2.setFullName("Jane Doe");
        customer2.setEmail("jane.doe@example.com");
        customer2.setPassword("password456");
    }

    @Test
    public void testSaveAndFindById() {
        // Save the customer
        customerRepository.save(customer1);

        // Verify that the customer was saved and can be retrieved
        Customer foundCustomer = customerRepository.findById(customer1.getId()).orElse(null);
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getUsername()).isEqualTo("john_doe");
    }

    @Test
    public void testFindByEmail() {
        // Save the customer
        customerRepository.save(customer1);

        // Find by email
        Customer foundCustomer = customerRepository.findByEmail("john.doe@example.com");
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testDeleteCustomer() {
        // Save the customer
        customerRepository.save(customer1);

        // Delete the customer
        customerRepository.delete(customer1);

        // Verify that the customer is deleted
        Customer foundCustomer = customerRepository.findById(customer1.getId()).orElse(null);
        assertThat(foundCustomer).isNull();
    }

    @Test
    public void testFindAllCustomers() {
        // Save customers
        customerRepository.save(customer1);
        customerRepository.save(customer2);

        // Verify that both customers are saved
        Iterable<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(2);
    }
}

