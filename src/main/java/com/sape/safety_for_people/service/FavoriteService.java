package com.sape.safety_for_people.service;

import com.sape.safety_for_people.model.Favorite;
import com.sape.safety_for_people.model.Product;
import com.sape.safety_for_people.model.User;
import com.sape.safety_for_people.repository.FavoriteRepository;
import com.sape.safety_for_people.repository.ProductRepository;
import com.sape.safety_for_people.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Long> obtenerProductIds(Long userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(favorito -> favorito.getProduct().getId())
                .toList();
    }

    @Transactional
    public void agregar(Long userId, Long productId) {
        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ID: " + productId));

        Favorite favorito = new Favorite();
        favorito.setUser(user);
        favorito.setProduct(product);
        favoriteRepository.save(favorito);
    }

    @Transactional
    public void quitar(Long userId, Long productId) {
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }
}
