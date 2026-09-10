package com.sape.safety_for_people.controller;

import com.sape.safety_for_people.dto.RoleRequestDTO;
import com.sape.safety_for_people.dto.RoleResponseDTO;
import com.sape.safety_for_people.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<RoleResponseDTO>> getActiveRoles() {
        return ResponseEntity.ok(roleService.findActiveOnly());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Integer id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody RoleRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(requestDTO));
    }
}