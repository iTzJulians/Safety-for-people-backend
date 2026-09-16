package com.sape.safety_for_people.controller;

import com.sape.safety_for_people.model.User;
import com.sape.safety_for_people.repository.UserRepository;
import com.sape.safety_for_people.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;

    public FavoriteController(FavoriteService favoriteService, UserRepository userRepository) {
        this.favoriteService = favoriteService;
        this.userRepository = userRepository;
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Long>> obtenerMisFavoritos(Authentication authentication) {
        User user = resolverUsuario(authentication);
        return ResponseEntity.ok(favoriteService.obtenerProductIds(user.getId()));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> agregarFavorito(@PathVariable Long productId, Authentication authentication) {
        User user = resolverUsuario(authentication);
        favoriteService.agregar(user.getId(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> quitarFavorito(@PathVariable Long productId, Authentication authentication) {
        User user = resolverUsuario(authentication);
        favoriteService.quitar(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }

    private User resolverUsuario(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado."));
    }
}
