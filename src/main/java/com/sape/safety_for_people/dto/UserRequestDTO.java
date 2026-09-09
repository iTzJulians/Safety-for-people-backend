package com.sape.safety_for_people.dto;

public record UserRequestDTO(
        String name,
        String email,
        String password,
        String phoneNumber,
        Integer roleId
){

}