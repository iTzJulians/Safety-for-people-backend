package com.sape.safety_for_people.dto;

import java.math.BigDecimal;
import java.util.List;

public record SaleRequestDTO(
        Integer quantity,
        BigDecimal totalAmount,
        Boolean active,
        Integer status,
        List<SaleItemRequestDTO> items
) {
}
