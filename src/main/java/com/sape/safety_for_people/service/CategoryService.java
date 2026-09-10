package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.CategoryRequestDTO;
import com.sape.safety_for_people.dto.CategoryResponseDTO;
import com.sape.safety_for_people.model.Category;
import com.sape.safety_for_people.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findActiveOnly() {
        return categoryRepository.findByActiveTrue().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        return mapToResponseDTO(category);
    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO requestDTO) {
        Category category = Category.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .active(requestDTO.getActive() != null ? requestDTO.getActive() : true)
                .build();

        Category saved = categoryRepository.save(category);
        return mapToResponseDTO(saved);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        category.setName(requestDTO.getName());
        category.setDescription(requestDTO.getDescription());
        if (requestDTO.getActive() != null) {
            category.setActive(requestDTO.getActive());
        }

        Category updated = categoryRepository.save(category);
        return mapToResponseDTO(updated);
    }

    @Transactional
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        category.setActive(false);
        categoryRepository.save(category);
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