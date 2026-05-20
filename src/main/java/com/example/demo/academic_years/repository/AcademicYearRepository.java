package com.example.demo.academic_years.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.academic_years.model.entity.AcademicYear;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, UUID> {
    List<AcademicYear> findByNameContainingIgnoreCase(String name);
    List<AcademicYear> findByCodeContainingIgnoreCase(String code);
    List<AcademicYear> findByIsActiveTrue();
}

