package com.sape.safety_for_people.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public record SaleRequestDTO(
        @NotNull(message = "La cantidad total es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a cero")
        Integer quantity,

        @NotNull(message = "El monto total es obligatorio")
        @Positive(message = "El monto total debe ser mayor a cero")
        BigDecimal totalAmount,

        Boolean active,

        @NotNull(message = "El ID del estado es obligatorio")
        Long statusId,

        @NotEmpty(message = "La venta debe incluir al menos un detalle de ítem")
        @Valid
        List<SaleItemRequestDTO> items
) {
}