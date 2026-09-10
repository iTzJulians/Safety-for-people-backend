package com.sape.safety_for_people.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleItemRequestDTO(
        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,

        @NotNull(message = "La cantidad del producto es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a cero")
        Integer quantity
) {
}