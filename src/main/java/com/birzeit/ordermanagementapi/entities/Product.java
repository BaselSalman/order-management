package com.birzeit.ordermanagementapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TINYTEXT")
    private String slug;

    @Column(columnDefinition = "TINYTEXT")
    private String name;

    @Column(columnDefinition = "TINYTEXT")
    private String reference;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal vat;

    private boolean stockable;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Stock> stocks;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<ProductOrder> productOrders;

}
