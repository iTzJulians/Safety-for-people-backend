package com.sape.safety_for_people.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDTO {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String name;

    @NotBlank(message = "La descripción de la categoría es obligatoria")
    private String description;

    private Boolean active;
}