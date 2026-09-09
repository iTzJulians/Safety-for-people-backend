package com.sape.safety_for_people.repository;

import com.sape.safety_for_people.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}