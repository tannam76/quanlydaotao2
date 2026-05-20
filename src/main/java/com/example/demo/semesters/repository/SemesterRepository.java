package com.example.demo.semesters.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.semesters.model.entity.Semester;

public interface SemesterRepository extends JpaRepository<Semester, UUID> {
    List<Semester> findByNameContainingIgnoreCase(String name);
    List<Semester> findByCodeContainingIgnoreCase(String code);
    List<Semester> findBySchoolYearId(UUID schoolYearId);
    List<Semester> findByIsActiveTrue();
}

