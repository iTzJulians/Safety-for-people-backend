package com.sape.safety_for_people.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        String phoneNumber,
        Boolean active,
        @NotNull Long roleId
) {}