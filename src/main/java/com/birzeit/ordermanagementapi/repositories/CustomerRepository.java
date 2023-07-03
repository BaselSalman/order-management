package com.birzeit.ordermanagementapi.repositories;

import com.birzeit.ordermanagementapi.entities.Customer;
import com.birzeit.ordermanagementapi.entities.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByUserCredentials(UserCredentials userCredentials);
}
