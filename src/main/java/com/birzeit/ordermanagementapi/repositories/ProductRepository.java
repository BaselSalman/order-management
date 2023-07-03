package com.birzeit.ordermanagementapi.repositories;

import com.birzeit.ordermanagementapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
