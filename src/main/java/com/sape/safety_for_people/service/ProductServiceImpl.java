package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.ProductRequestDTO;
import com.sape.safety_for_people.dto.ProductResponseDTO;
import com.sape.safety_for_people.mapper.ProductMapper;
import com.sape.safety_for_people.model.Category;
import com.sape.safety_for_people.model.Group;
import com.sape.safety_for_people.model.Product;
import com.sape.safety_for_people.repository.CategoryRepository;
import com.sape.safety_for_people.repository.GroupRepository;
import com.sape.safety_for_people.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getActiveOnly() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getFavorites() {
        return productRepository.findByFavoriteTrue()
                .stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getById(Long id) {
        Product product = findEntityById(id);
        return ProductMapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = ProductMapper.toEntity(dto);

        assignCategoryAndGroup(product, dto);

        Product saved = productRepository.save(product);
        return ProductMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = findEntityById(id);

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setFavorite(dto.getFavorite() != null ? dto.getFavorite() : false);
        product.setImage(dto.getImage());
        product.setBackgroundColor(dto.getBackgroundColor());
        product.setActive(dto.getActive() != null ? dto.getActive() : true);
        product.setCharacteristics(dto.getCharacteristics());

        assignCategoryAndGroup(product, dto);

        return ProductMapper.toDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = findEntityById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    private Product findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    private void assignCategoryAndGroup(Product product, ProductRequestDTO dto) {
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + dto.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        if (dto.getGroupId() != null) {
            Group group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado con ID: " + dto.getGroupId()));
            product.setGroup(group);
        } else {
            product.setGroup(null);
        }
    }
}