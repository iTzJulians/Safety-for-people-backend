package com.sape.safety_for_people.dto;

import java.math.BigDecimal;

public record SaleDetailResponseDTO(
        Long id,
        Long productId,
        String productName,
        BigDecimal price,
        Integer quantity
) {
}