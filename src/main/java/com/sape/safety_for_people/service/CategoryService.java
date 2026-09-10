package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.CategoryRequestDTO;
import com.sape.safety_for_people.dto.CategoryResponseDTO;
import com.sape.safety_for_people.model.Category;
import com.sape.safety_for_people.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDTO> findActiveOnly() {
        return categoryRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO findById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        return mapToResponseDTO(category);
    }

    public CategoryResponseDTO create(CategoryRequestDTO requestDTO) {
        Category category = Category.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .active(requestDTO.getActive() != null ? requestDTO.getActive() : true)
                .build();

        Category saved = categoryRepository.save(category);
        return mapToResponseDTO(saved);
    }

    private CategoryResponseDTO mapToResponseDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .createdOn(category.getCreatedOn())
                .build();
    }
}