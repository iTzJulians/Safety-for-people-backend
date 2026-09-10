package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.SaleDetailResponseDTO;
import com.sape.safety_for_people.dto.SaleItemRequestDTO;
import com.sape.safety_for_people.dto.SaleRequestDTO;
import com.sape.safety_for_people.dto.SaleResponseDTO;
import com.sape.safety_for_people.model.Product;
import com.sape.safety_for_people.model.SaleDetail;
import com.sape.safety_for_people.model.Sales;
import com.sape.safety_for_people.model.Status;
import com.sape.safety_for_people.repository.ProductRepository;
import com.sape.safety_for_people.repository.SaleDetailRepository;
import com.sape.safety_for_people.repository.SaleRepository;
import com.sape.safety_for_people.repository.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final ProductRepository productRepository;
    private final StatusRepository statusRepository;

    public SaleService(SaleRepository saleRepository,
                       SaleDetailRepository saleDetailRepository,
                       ProductRepository productRepository,
                       StatusRepository statusRepository) {
        this.saleRepository = saleRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.productRepository = productRepository;
        this.statusRepository = statusRepository;
    }

    public List<SaleResponseDTO> findAll() {
        return saleRepository.findAll().stream().map(this::toResponse).toList();
    }

    public SaleResponseDTO findById(Long id) {
        return toResponse(findEntityById(id));
    }

    @Transactional
    public SaleResponseDTO create(SaleRequestDTO request) {
        Sales sale = new Sales();
        applyItems(request, sale);
        return toResponse(saleRepository.save(sale));
    }

    @Transactional
    public SaleResponseDTO update(Long id, SaleRequestDTO request) {
        Sales sale = findEntityById(id);
        sale.getDetails().clear();
        applyItems(request, sale);
        return toResponse(saleRepository.save(sale));
    }

    public void delete(Long id) {
        saleRepository.delete(findEntityById(id));
    }

    private Sales findEntityById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found: " + id));
    }

    private void applyItems(SaleRequestDTO request, Sales sale) {
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        List<SaleItemRequestDTO> items = request.items() == null ? List.of() : request.items();
        for (SaleItemRequestDTO item : items) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.productId()));
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
        sale.setActive(request.active());
        Status status = statusRepository.findById(request.status().longValue())
                .orElseThrow(() -> new IllegalArgumentException("Status not found: " + request.status()));
        sale.setStatus(status);
    }

    private SaleResponseDTO toResponse(Sales sale) {
        List<SaleDetailResponseDTO> details = saleDetailRepository.findBySaleId(sale.getId()).stream()
                .map(detail -> new SaleDetailResponseDTO(
                        detail.getId(),
                        detail.getProduct().getId(),
                        detail.getProduct().getName(),
                        detail.getPrice(),
                        detail.getQuantity()
                ))
                .toList();
        return new SaleResponseDTO(
                sale.getId(),
                sale.getCreatedOn(),
                sale.getQuantity(),
                sale.getTotalAmount(),
                sale.getActive(),
                sale.getStatus() != null ? sale.getStatus().getId().intValue() : null,
                details
        );
    }
}