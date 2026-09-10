package com.sape.safety_for_people.repository;

import com.sape.safety_for_people.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    // Genera la consulta SELECT * FROM status WHERE active = true
    List<Status> findByActiveTrue();
}