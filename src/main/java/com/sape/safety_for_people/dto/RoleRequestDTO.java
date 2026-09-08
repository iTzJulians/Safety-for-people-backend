package com.sape.safety_for_people.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequestDTO {

    @NotBlank(message = "El nombre del rol es obligatorio")
    private String name;

    private Boolean active;
}