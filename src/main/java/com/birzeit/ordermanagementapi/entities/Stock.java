package com.birzeit.ordermanagementapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock implements Comparable<Stock>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private LocalDateTime updatedAt;

    @Override
    public int compareTo(Stock stock) {
        if(this.updatedAt.isBefore(stock.getUpdatedAt()))
            return -1;
        else if(this.updatedAt.isAfter(stock.getUpdatedAt()))
            return 1;
        return 0;
    }
}
