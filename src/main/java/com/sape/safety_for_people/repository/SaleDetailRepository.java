package com.sape.safety_for_people.repository;

import com.sape.safety_for_people.model.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {

    List<SaleDetail> findBySaleId(Long saleId);
}