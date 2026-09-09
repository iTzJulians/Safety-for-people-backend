package com.sape.safety_for_people.controller;

import com.sape.safety_for_people.dto.SaleRequestDTO;
import com.sape.safety_for_people.dto.SaleResponseDTO;
import com.sape.safety_for_people.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public List<SaleResponseDTO> findAll() {
        return saleService.findAll();
    }

    @GetMapping("/{id}")
    public SaleResponseDTO findById(@PathVariable Long id) {
        return saleService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleResponseDTO create(@RequestBody SaleRequestDTO request) {
        return saleService.create(request);
    }

    @PutMapping("/{id}")
    public SaleResponseDTO update(@PathVariable Long id, @RequestBody SaleRequestDTO request) {
        return saleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        saleService.delete(id);
    }
}
