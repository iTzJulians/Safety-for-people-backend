package com.sape.safety_for_people.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleResponseDTO(
        Long id,
        LocalDateTime createdOn,
        Integer quantity,
        BigDecimal totalAmount,
        Boolean active,
        Integer status,
        List<SaleDetailResponseDTO> details
) {
}
