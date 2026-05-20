package com.example.demo.users.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.users.model.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByUsername(String username);
    Optional<User> findFirstByUsername(String username);
}
