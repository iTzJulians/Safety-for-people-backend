package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.ProductRequestDTO;
import com.sape.safety_for_people.dto.ProductResponseDTO;
import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAll();
    ProductResponseDTO getById(Long id);
    ProductResponseDTO create(ProductRequestDTO dto);
    ProductResponseDTO update(Long id, ProductRequestDTO dto);
    void delete(Long id);
}