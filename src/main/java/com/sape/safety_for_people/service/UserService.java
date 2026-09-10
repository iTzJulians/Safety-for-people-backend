package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.UserRequestDTO;
import com.sape.safety_for_people.dto.UserResponseDTO;
import com.sape.safety_for_people.model.Role;
import com.sape.safety_for_people.model.User;
import com.sape.safety_for_people.repository.RoleRepository;
import com.sape.safety_for_people.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getActiveOnly() {
        return userRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO datos) {
        User user = new User();
        user.setName(datos.name());
        user.setEmail(datos.email());
        user.setPassword(datos.password());
        user.setPhoneNumber(datos.phoneNumber());
        user.setActive(datos.active() != null ? datos.active() : true);

        if (datos.roleId() != null) {
            Role role = roleRepository.findById(datos.roleId())
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + datos.roleId()));
            user.setRole(role);
        }

        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    @Transactional
    public Optional<UserResponseDTO> updateUser(Long id, UserRequestDTO datos) {
        return userRepository.findById(id).map(user -> {
            user.setName(datos.name());
            user.setEmail(datos.email());
            user.setPhoneNumber(datos.phoneNumber());
            if (datos.active() != null) {
                user.setActive(datos.active());
            }

            if (datos.roleId() != null) {
                Role role = roleRepository.findById(datos.roleId())
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + datos.roleId()));
                user.setRole(role);
            }

            User updatedUser = userRepository.save(user);
            return mapToResponseDTO(updatedUser);
        });
    }

    @Transactional
    public boolean deleteUser(Long id) {
        return userRepository.findById(id).map(user -> {
            user.setActive(false);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getActive(),
                user.getRole() != null ? user.getRole().getId() : null
        );
    }
}