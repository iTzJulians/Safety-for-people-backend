package com.sape.safety_for_people.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Boolean favorite;
    private String image;
    private String backgroundColor;
    private Boolean active;
    private String characteristics;
}