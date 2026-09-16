package com.sape.safety_for_people.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String nombre;

    @NotBlank(message = "El número de celular es obligatorio")
    private String phoneNumber;

    private Long roleId;
}