package com.sape.safety_for_people.mapper;

import com.sape.safety_for_people.dto.ProductRequestDTO;
import com.sape.safety_for_people.dto.ProductResponseDTO;
import com.sape.safety_for_people.model.Product;
import java.time.LocalDateTime;

public class ProductMapper {

    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setFavorite(dto.getFavorite());
        product.setImage(dto.getImage());
        product.setBackgroundColor(dto.getBackgroundColor());
        product.setActive(dto.getActive());
        product.setCharacteristics(dto.getCharacteristics());
        product.setCreatedOn(LocalDateTime.now());
        return product;
    }

    public static ProductResponseDTO toDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setFavorite(product.getFavorite());
        dto.setImage(product.getImage());
        dto.setBackgroundColor(product.getBackgroundColor());
        dto.setActive(product.getActive());
        dto.setCreatedOn(product.getCreatedOn());
        dto.setCharacteristics(product.getCharacteristics());
        return dto;
    }
}