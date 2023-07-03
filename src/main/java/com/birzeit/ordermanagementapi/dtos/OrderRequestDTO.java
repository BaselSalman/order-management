package com.birzeit.ordermanagementapi.dtos;

import java.util.List;

public record OrderRequestDTO(List<ProductOrderRequestDTO> orders) {
}
