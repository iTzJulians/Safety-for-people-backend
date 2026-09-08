package com.sape.safety_for_people.repository;

import com.sape.safety_for_people.model.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sales, Integer> {
}
