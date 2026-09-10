package com.sape.safety_for_people.dto;

public record SaleItemRequestDTO(
        Long productId,
        Integer quantity
) {
}