package com.sape.safety_for_people.controllers;

import com.sape.safety_for_people.dto.UserResponseDTO;
import com.sape.safety_for_people.model.User;
import com.sape.safety_for_people.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<ResponseEntity<Object>> getUserById(@PathVariable Integer id) {
        Optional<UserResponseDTO> user = userService.getUserById(id);
        return user.map(userService -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        Optional<User> userOptional = userService.getUserById(id);

        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = userOptional.get();
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPassword(userDetails.getPassword());
        existingUser.setPhoneNumber(userDetails.getPhoneNumber());
        existingUser.setActive(userDetails.getActive());
        existingUser.setRoleId(userDetails.getRoleId());

        User updatedUser = userService.saveUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}

