package com.sape.safety_for_people.dto;

import java.time.LocalDateTime;

public record RoleResponseDTO(
        Long id,
        String name,
        Boolean active,
        LocalDateTime createdOn
) {
}