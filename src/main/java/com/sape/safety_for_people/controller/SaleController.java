package com.sape.safety_for_people.controller;

import com.sape.safety_for_people.dto.SaleRequestDTO;
import com.sape.safety_for_people.dto.SaleResponseDTO;
import com.sape.safety_for_people.model.User;
import com.sape.safety_for_people.repository.UserRepository;
import com.sape.safety_for_people.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final UserRepository userRepository;

    public SaleController(SaleService saleService, UserRepository userRepository) {
        this.saleService = saleService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> findAll() {
        return ResponseEntity.ok(saleService.findAll());
    }

    @GetMapping("/mine")
    public ResponseEntity<List<SaleResponseDTO>> findMine(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado."));
        return ResponseEntity.ok(saleService.findByUserId(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> create(@Valid @RequestBody SaleRequestDTO request) {
        SaleResponseDTO created = saleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SaleRequestDTO request
    ) {
        return ResponseEntity.ok(saleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}