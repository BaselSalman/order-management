package com.birzeit.ordermanagementapi.dtos;

import java.math.BigDecimal;
import java.math.BigInteger;

public record ProductRequestDTO(String slug, String name, String reference, BigDecimal price, BigDecimal vat) {
}
