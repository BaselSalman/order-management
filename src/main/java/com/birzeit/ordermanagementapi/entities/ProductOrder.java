package com.birzeit.ordermanagementapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrder {

    @EmbeddedId
    private ProductOrderId id;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    @ManyToOne
    @MapsId("orderId")
    private Order order;

    private int quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal vat;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductOrderId implements Serializable {

        private int productId;
        private int orderId;
    }
}


