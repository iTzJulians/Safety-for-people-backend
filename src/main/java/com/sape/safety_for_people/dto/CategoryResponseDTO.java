package com.sape.safety_for_people.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private LocalDateTime createdOn;
}