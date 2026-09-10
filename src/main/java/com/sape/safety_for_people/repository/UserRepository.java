package com.sape.safety_for_people.repository;

import com.sape.safety_for_people.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByActiveTrue();
}