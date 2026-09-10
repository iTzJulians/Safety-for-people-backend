package com.sape.safety_for_people.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio debe ser un número positivo o cero")
    private BigDecimal price;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    private Boolean favorite;

    private String image;

    private String backgroundColor;

    private Boolean active;

    private String characteristics;

    private Long categoryId;

    private Long groupId;
}