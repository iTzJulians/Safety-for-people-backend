package com.sape.safety_for_people.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Boolean favorite;
    private String image;
    private String backgroundColor;
    private Boolean active;
    private LocalDateTime createdOn; // <-- Agrega este campo
    private String characteristics;
    private Long categoryId;
    private Long groupId;
}