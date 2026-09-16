package com.sape.safety_for_people.repository;

import com.sape.safety_for_people.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByActiveTrue();

    List<Sale> findByUserIdOrderByCreatedOnDesc(Long userId);

    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.active = true")
    BigDecimal sumTotalAmountOfActiveSales();

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.active = true")
    Long countActiveSales();

    @Query("SELECT s FROM Sale s WHERE s.active = true ORDER BY s.createdOn DESC")
    List<Sale> findRecentActiveSales();

    @Query("SELECT s.status.id, s.status.name, COUNT(s) FROM Sale s WHERE s.active = true GROUP BY s.status.id, s.status.name")
    List<Object[]> countSalesByStatus();

    @Query("SELECT s.status.name, COUNT(s) FROM Sale s WHERE s.active = true GROUP BY s.status.name")
    List<Object[]> countSalesGroupedByStatus();
}