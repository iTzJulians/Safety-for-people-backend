package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.RoleRequestDTO;
import com.sape.safety_for_people.dto.RoleResponseDTO;
import com.sape.safety_for_people.model.Role;
import com.sape.safety_for_people.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleResponseDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<RoleResponseDTO> findActiveOnly() {
        return roleRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public RoleResponseDTO findById(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
        return mapToResponseDTO(role);
    }

    public RoleResponseDTO create(RoleRequestDTO requestDTO) {
        Role role = Role.builder()
                .name(requestDTO.getName())
                .active(requestDTO.getActive() != null ? requestDTO.getActive() : true)
                .build();

        Role saved = roleRepository.save(role);
        return mapToResponseDTO(saved);
    }

    private RoleResponseDTO mapToResponseDTO(Role role) {
        return RoleResponseDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .active(role.getActive())
                .createdOn(role.getCreatedOn())
                .build();
    }
}