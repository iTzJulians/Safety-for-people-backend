package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.StatusRequestDTO;
import com.sape.safety_for_people.dto.StatusResponseDTO;
import com.sape.safety_for_people.model.Status;
import com.sape.safety_for_people.repository.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Transactional(readOnly = true)
    public List<StatusResponseDTO> getAllStatuses() {
        return statusRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StatusResponseDTO> getActiveOnly() {
        return statusRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<StatusResponseDTO> getStatusById(Long id) {
        return statusRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    @Transactional
    public StatusResponseDTO createStatus(StatusRequestDTO datos) {
        Status status = new Status();
        status.setName(datos.getName());
        status.setActive(datos.getActive() != null ? datos.getActive() : true);

        Status savedStatus = statusRepository.save(status);
        return mapToResponseDTO(savedStatus);
    }

    @Transactional
    public Optional<StatusResponseDTO> updateStatus(Long id, StatusRequestDTO datos) {
        return statusRepository.findById(id).map(status -> {
            status.setName(datos.getName());
            if (datos.getActive() != null) {
                status.setActive(datos.getActive());
            }
            Status updatedStatus = statusRepository.save(status);
            return mapToResponseDTO(updatedStatus);
        });
    }

    @Transactional
    public boolean deleteStatus(Long id) {
        return statusRepository.findById(id).map(status -> {
            status.setActive(false);
            statusRepository.save(status);
            return true;
        }).orElse(false);
    }

    private StatusResponseDTO mapToResponseDTO(Status status) {
        return new StatusResponseDTO(
                status.getId(),
                status.getName(),
                status.getCreatedOn(),
                status.getActive()
        );
    }
}