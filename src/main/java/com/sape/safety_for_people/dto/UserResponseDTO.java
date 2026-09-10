package com.sape.safety_for_people.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String phoneNumber,
        Boolean active,
        Long roleId
) {}