package com.pjsh.vrs.storage;

import com.pjsh.vrs.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query
    Customer findByUsername(String username);

    Customer findByEmail(String email);
}

