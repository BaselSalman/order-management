package com.birzeit.ordermanagementapi.repositories;

import com.birzeit.ordermanagementapi.entities.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
}
