package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.UserRequestDTO;
import com.sape.safety_for_people.dto.UserResponseDTO;
import com.sape.safety_for_people.model.User;
import com.sape.safety_for_people.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

     private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

     @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

     @Transactional(readOnly = true)
    public Optional<UserResponseDTO> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

     @Transactional
    public UserResponseDTO createUser(UserRequestDTO datos) {
        User user = new User();
        user.setName(datos.name());
        user.setEmail(datos.email());
        user.setPassword(datos.password()); // Nota: En un proyecto real, aquí se encriptaría con BCrypt
        user.setPhoneNumber(datos.phoneNumber());
        user.setRoleId(datos.roleId());

        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    // Update
    @Transactional
    public Optional<UserResponseDTO> updateUser(Integer id, UserRequestDTO datos) {
        return userRepository.findById(id).map(user -> {
            user.setName(datos.name());
            user.setEmail(datos.email());
            user.setPhoneNumber(datos.phoneNumber());
            user.setRoleId(datos.roleId());
            // La contraseña suele actualizarse en un endpoint aparte por seguridad, pero puedes agregarla aquí si lo deseas

            User updatedUser = userRepository.save(user);
            return mapToResponseDTO(updatedUser);
        });
    }

     @Transactional
    public boolean deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

     private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getActive(),
                user.getRoleId()
        );
    }
}