package com.birzeit.ordermanagementapi.repositories;

import com.birzeit.ordermanagementapi.entities.Product;
import com.birzeit.ordermanagementapi.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findByProduct(Product product);

    @Query("select count(s) from Stock s where s.product = :product")
    int findHowManyStocksAvailableForProduct(@Param("product") Product product);

}
