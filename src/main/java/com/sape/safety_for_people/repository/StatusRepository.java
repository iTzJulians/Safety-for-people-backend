package com.sape.safety_for_people.repository;

import com.sape.safety_for_people.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    List<Status> findByActiveTrue();

    Optional<Status> findByName(String name);
}