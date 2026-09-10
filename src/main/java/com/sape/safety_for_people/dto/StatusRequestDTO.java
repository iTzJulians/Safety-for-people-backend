package com.sape.safety_for_people.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusRequestDTO {

    @NotBlank(message = "El nombre del estado es obligatorio")
    private String name;

    private Boolean active;
}