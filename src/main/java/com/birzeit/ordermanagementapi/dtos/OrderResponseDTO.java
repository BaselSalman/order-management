package com.birzeit.ordermanagementapi.dtos;

import java.util.List;

public record OrderResponseDTO(int id, CustomerResponseDTO customer, String orderedAt, List<ProductOrderResponseDTO> productOrderList) {
}
