package com.sape.safety_for_people.service.impl;

import com.sape.safety_for_people.dto.ProductRequestDTO;
import com.sape.safety_for_people.dto.ProductResponseDTO;
import com.sape.safety_for_people.model.Product;
import com.sape.safety_for_people.mapper.ProductMapper;
import com.sape.safety_for_people.repository.ProductRepository;
import com.sape.safety_for_people.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductResponseDTO> getAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return ProductMapper.toDTO(product);
    }

    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = ProductMapper.toEntity(dto);

        Product saved = productRepository.save(product);
        return ProductMapper.toDTO(saved);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setFavorite(dto.getFavorite());
        product.setImage(dto.getImage());
        product.setBackgroundColor(dto.getBackgroundColor());
        product.setActive(dto.getActive());
        product.setCharacteristics(dto.getCharacteristics());

        return ProductMapper.toDTO(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}