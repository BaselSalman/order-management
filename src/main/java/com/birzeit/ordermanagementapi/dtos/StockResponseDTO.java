package com.birzeit.ordermanagementapi.dtos;

import com.birzeit.ordermanagementapi.entities.Product;

public record StockResponseDTO(int id, Product product, int quantity, String updatedAt) {
}
