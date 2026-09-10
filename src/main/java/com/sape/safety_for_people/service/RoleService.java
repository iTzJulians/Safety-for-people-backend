package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.RoleRequestDTO;
import com.sape.safety_for_people.dto.RoleResponseDTO;
import com.sape.safety_for_people.model.Role;
import com.sape.safety_for_people.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> findActiveOnly() {
        return roleRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoleResponseDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
        return mapToResponseDTO(role);
    }

    @Transactional
    public RoleResponseDTO create(RoleRequestDTO requestDTO) {
        Role role = Role.builder()
                .name(requestDTO.getName())
                .active(requestDTO.getActive() != null ? requestDTO.getActive() : true)
                .build();

        Role saved = roleRepository.save(role);
        return mapToResponseDTO(saved);
    }

    @Transactional
    public RoleResponseDTO update(Long id, RoleRequestDTO requestDTO) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));

        role.setName(requestDTO.getName());
        if (requestDTO.getActive() != null) {
            role.setActive(requestDTO.getActive());
        }

        Role updated = roleRepository.save(role);
        return mapToResponseDTO(updated);
    }

    @Transactional
    public void deleteById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));

        role.setActive(false);
        roleRepository.save(role);
    }

    private RoleResponseDTO mapToResponseDTO(Role role) {
        if (role == null) {
            return null;
        }

        return new RoleResponseDTO(
                role.getId(),
                role.getName(),
                role.getActive(),
                role.getCreatedOn()
        );
    }
}