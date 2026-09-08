package com.sape.safety_for_people.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleResponseDTO(
        Integer id,
        LocalDateTime createdOn,
        Integer quantity,
        BigDecimal totalAmount,
        Boolean active,
        Integer status
) {
}
