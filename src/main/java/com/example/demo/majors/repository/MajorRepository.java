package com.example.demo.majors.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.majors.model.entity.Major;

public interface MajorRepository extends JpaRepository<Major, UUID> {
    List<Major> findByNameContainingIgnoreCase(String name);
    List<Major> findByCodeContainingIgnoreCase(String code);
    List<Major> findByDepartmentId(UUID departmentId);
    boolean existsByCodeIgnoreCase(String code);
}

