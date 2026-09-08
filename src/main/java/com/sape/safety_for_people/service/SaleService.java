package com.sape.safety_for_people.service;

import com.sape.safety_for_people.dto.SaleRequestDTO;
import com.sape.safety_for_people.dto.SaleResponseDTO;
import com.sape.safety_for_people.model.Sales;
import com.sape.safety_for_people.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public List<SaleResponseDTO> findAll() {
        return saleRepository.findAll().stream().map(this::toResponse).toList();
    }

    public SaleResponseDTO findById(Integer id) {
        return toResponse(findEntityById(id));
    }

    public SaleResponseDTO create(SaleRequestDTO request) {
        Sales sale = new Sales();
        copyRequest(request, sale);
        return toResponse(saleRepository.save(sale));
    }

    public SaleResponseDTO update(Integer id, SaleRequestDTO request) {
        Sales sale = findEntityById(id);
        copyRequest(request, sale);
        return toResponse(saleRepository.save(sale));
    }

    public void delete(Integer id) {
        saleRepository.delete(findEntityById(id));
    }

    private Sales findEntityById(Integer id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found: " + id));
    }

    private void copyRequest(SaleRequestDTO request, Sales sale) {
        sale.setQuantity(request.quantity());
        sale.setTotalAmount(request.totalAmount());
        sale.setActive(request.active());
        sale.setStatus(request.status());
    }

    private SaleResponseDTO toResponse(Sales sale) {
        return new SaleResponseDTO(
                sale.getId(), sale.getCreatedOn(), sale.getQuantity(), sale.getTotalAmount(),
                sale.getActive(), sale.getStatus()
        );
    }
}
