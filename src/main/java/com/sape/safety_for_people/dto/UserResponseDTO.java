package com.sape.safety_for_people.dto;

public record UserResponseDTO(
        Integer id,
        String name,
        String email,
        String phoneNumber,
        Boolean active,
        Integer roleId
){

}