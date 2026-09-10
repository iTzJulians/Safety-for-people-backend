package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.ProductRequestDTO;
import com.sape.safety_for_people.dto.ProductResponseDTO;
import com.sape.safety_for_people.model.Product;
import com.sape.safety_for_people.model.Category;
import com.sape.safety_for_people.model.Group;
import com.sape.safety_for_people.model.ProductMapper;
import com.sape.safety_for_people.repository.ProductRepository;
import com.sape.safety_for_people.repository.CategoryRepository;
import com.sape.safety_for_people.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GroupRepository groupRepository;

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

        // Buscar y asignar Categoría si el DTO trae el ID
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            product.setCategory(category);
        }

        // Buscar y asignar Grupo si el DTO trae el ID
        if (dto.getGroupId() != null) {
            Group group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
            product.setGroup(group);
        }

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

        // Actualizar Categoría si el DTO trae el ID
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            product.setCategory(category);
        }

        // Actualizar Grupo si el DTO trae el ID
        if (dto.getGroupId() != null) {
            Group group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
            product.setGroup(group);
        }

        return ProductMapper.toDTO(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}