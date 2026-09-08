package com.sape.safety_for_people.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {

    private Integer id;
    private String name;
    private Boolean active;
    private LocalDateTime createdOn;
}