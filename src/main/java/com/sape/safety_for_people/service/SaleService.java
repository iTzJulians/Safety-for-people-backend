package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.SaleDetailResponseDTO;
import com.sape.safety_for_people.dto.SaleItemRequestDTO;
import com.sape.safety_for_people.dto.SaleRequestDTO;
import com.sape.safety_for_people.dto.SaleResponseDTO;
import com.sape.safety_for_people.model.Product;
import com.sape.safety_for_people.model.Sale;
import com.sape.safety_for_people.model.SaleDetail;
import com.sape.safety_for_people.model.Status;
import com.sape.safety_for_people.model.User;
import com.sape.safety_for_people.repository.ProductRepository;
import com.sape.safety_for_people.repository.SaleDetailRepository;
import com.sape.safety_for_people.repository.SaleRepository;
import com.sape.safety_for_people.repository.StatusRepository;
import com.sape.safety_for_people.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final ProductRepository productRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    public SaleService(SaleRepository saleRepository,
                       SaleDetailRepository saleDetailRepository,
                       ProductRepository productRepository,
                       StatusRepository statusRepository,
                       UserRepository userRepository) {
        this.saleRepository = saleRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.productRepository = productRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<SaleResponseDTO> findAll() {
        return saleRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SaleResponseDTO> findActiveOnly() {
        return saleRepository.findByActiveTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SaleResponseDTO findById(Long id) {
        return toResponse(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<SaleResponseDTO> findByUserId(Long userId) {
        return saleRepository.findByUserIdOrderByCreatedOnDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SaleResponseDTO create(SaleRequestDTO request) {
        Sale sale = new Sale();
        if (sale.getDetails() == null) {
            sale.setDetails(new ArrayList<>());
        }
        applyItems(request, sale);
        return toResponse(saleRepository.save(sale));
    }

    @Transactional
    public SaleResponseDTO update(Long id, SaleRequestDTO request) {
        Sale sale = findEntityById(id);

        // Antes de reaplicar los ítems, se restaura el stock de los ítems actuales
        // para que actualizar una venta (p. ej. solo el estado) no descuente el stock dos veces.
        if (sale.getDetails() != null) {
            for (SaleDetail detalleAnterior : sale.getDetails()) {
                Product producto = detalleAnterior.getProduct();
                if (producto != null && producto.getStock() != null) {
                    producto.setStock(producto.getStock() + detalleAnterior.getQuantity());
                }
            }
            sale.getDetails().clear();
        } else {
            sale.setDetails(new ArrayList<>());
        }

        applyItems(request, sale);
        return toResponse(saleRepository.save(sale));
    }

    @Transactional
    public void delete(Long id) {
        Sale sale = findEntityById(id);
        sale.setActive(false);
        saleRepository.save(sale);
    }

    private Sale findEntityById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada con ID: " + id));
    }

    private void applyItems(SaleRequestDTO request, Sale sale) {
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        List<SaleItemRequestDTO> items = request.items() == null ? List.of() : request.items();
        for (SaleItemRequestDTO item : items) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + item.productId()));

            if (product.getStock() != null && product.getStock() < item.quantity()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + product.getName());
            }

            // Descuento de stock
            product.setStock(product.getStock() - item.quantity());

            SaleDetail detail = new SaleDetail();
            detail.setSale(sale);
            detail.setProduct(product);
            detail.setPrice(product.getPrice());
            detail.setQuantity(item.quantity());

            sale.getDetails().add(detail);

            totalQuantity += item.quantity();
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
        }

        sale.setQuantity(totalQuantity);
        sale.setTotalAmount(totalAmount);
        sale.setActive(request.active() != null ? request.active() : true);

        if (request.statusId() != null) {
            Status status = statusRepository.findById(request.statusId())
                    .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado con ID: " + request.statusId()));
            sale.setStatus(status);
        } else {
            sale.setStatus(null);
        }

        if (request.userId() != null) {
            User user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + request.userId()));
            sale.setUser(user);
        }
    }

    private SaleResponseDTO toResponse(Sale sale) {
        List<SaleDetailResponseDTO> details = saleDetailRepository.findBySaleId(sale.getId()).stream()
                .map(detail -> new SaleDetailResponseDTO(
                        detail.getId(),
                        detail.getProduct().getId(),
                        detail.getProduct().getName(),
                        detail.getPrice(),
                        detail.getQuantity()
                ))
                .toList();

        Long statusId = (sale.getStatus() != null) ? sale.getStatus().getId() : null;
        Long userId = (sale.getUser() != null) ? sale.getUser().getId() : null;

        return new SaleResponseDTO(
                sale.getId(),
                sale.getCreatedOn(),
                sale.getQuantity(),
                sale.getTotalAmount(),
                sale.getActive(),
                statusId,
                userId,
                details
        );
    }
}