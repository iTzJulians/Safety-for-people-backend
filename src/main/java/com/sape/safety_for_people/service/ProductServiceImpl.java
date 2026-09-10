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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              GroupRepository groupRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.groupRepository = groupRepository;
    }

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
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return ProductMapper.toDTO(product);
    }

    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = ProductMapper.toEntity(dto);
        assignCategoryAndGroup(product, dto.getCategoryId(), dto.getGroupId());

        Product saved = productRepository.save(product);
        return ProductMapper.toDTO(saved);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setFavorite(dto.getFavorite());
        product.setImage(dto.getImage());
        product.setBackgroundColor(dto.getBackgroundColor());
        product.setActive(dto.getActive());
        product.setCharacteristics(dto.getCharacteristics());

        assignCategoryAndGroup(product, dto.getCategoryId(), dto.getGroupId());

        return ProductMapper.toDTO(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado para eliminar con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    private void assignCategoryAndGroup(Product product, Long categoryId, Long groupId) {
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + categoryId));
            product.setCategory(category);
        }

        if (groupId != null) {
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new RuntimeException("Grupo no encontrado con ID: " + groupId));
            product.setGroup(group);
        }
    }
}