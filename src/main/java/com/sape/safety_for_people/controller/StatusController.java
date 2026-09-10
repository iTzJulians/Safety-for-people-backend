package com.sape.safety_for_people.controller;

import com.sape.safety_for_people.dto.StatusRequestDTO;
import com.sape.safety_for_people.dto.StatusResponseDTO;
import com.sape.safety_for_people.service.StatusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public ResponseEntity<List<StatusResponseDTO>> getAllStatuses() {
        return ResponseEntity.ok(statusService.getAllStatuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> getStatusById(@PathVariable Long id) {
        return statusService.getStatusById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StatusResponseDTO> createStatus(@Valid @RequestBody StatusRequestDTO requestDTO) {
        StatusResponseDTO created = statusService.createStatus(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusRequestDTO requestDTO
    ) {
        return statusService.updateStatus(id, requestDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        return statusService.deleteStatus(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}