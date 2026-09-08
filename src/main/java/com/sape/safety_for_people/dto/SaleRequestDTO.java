package com.sape.safety_for_people.dto;

import java.math.BigDecimal;

public record SaleRequestDTO(
        Integer quantity,
        BigDecimal totalAmount,
        Boolean active,
        Integer status
) {
}
