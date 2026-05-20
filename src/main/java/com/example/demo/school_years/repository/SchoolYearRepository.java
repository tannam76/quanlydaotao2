package com.example.demo.school_years.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.school_years.model.entity.SchoolYear;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, UUID> {
    List<SchoolYear> findByNameContainingIgnoreCase(String name);
    List<SchoolYear> findByCodeContainingIgnoreCase(String code);
    List<SchoolYear> findByIsActiveTrue();
}

