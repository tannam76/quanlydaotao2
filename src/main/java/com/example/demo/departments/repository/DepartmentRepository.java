package com.example.demo.departments.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.departments.model.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    List<Department> findByNameContainingIgnoreCase(String name);
    List<Department> findByCodeContainingIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);
}

