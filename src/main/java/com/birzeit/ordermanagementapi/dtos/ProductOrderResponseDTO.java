package com.birzeit.ordermanagementapi.dtos;

import java.math.BigDecimal;

public record ProductOrderResponseDTO(int productId, int quantity, BigDecimal price, BigDecimal vat) {
}
