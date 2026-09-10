package com.sape.safety_for_people.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusResponseDTO {
    private Long id;
    private String name;
    private LocalDateTime createdOn;
    private Boolean active;
}