package com.birzeit.ordermanagementapi.repositories;

import com.birzeit.ordermanagementapi.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
