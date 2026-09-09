package com.sape.safety_for_people.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private Boolean favorite;

    private String image;

    @Column(name = "background_color")
    private String backgroundColor;

    private Boolean active;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    private String characteristics; // en el ERD es tipo "json"
}