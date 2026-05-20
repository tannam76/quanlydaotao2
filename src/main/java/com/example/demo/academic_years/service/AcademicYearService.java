package com.example.demo.academic_years.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.academic_years.model.entity.AcademicYear;
import com.example.demo.academic_years.repository.AcademicYearRepository;

@Service
public class AcademicYearService {
    private final AcademicYearRepository repository;

    public AcademicYearService(AcademicYearRepository repository) {
        this.repository = repository;
    }

    public List<AcademicYear> getAll() {
        return repository.findAll();
    }

    public List<AcademicYear> getActive() {
        return repository.findByIsActiveTrue();
    }

    public Optional<AcademicYear> getById(UUID id) {
        return repository.findById(id);
    }

    public AcademicYear create(AcademicYear academicYear) {
        academicYear.setCreatedAt(java.time.LocalDateTime.now());
        return repository.save(academicYear);
    }

    public AcademicYear update(UUID id, AcademicYear academicYear) {
        return repository.findById(id)
            .map(existing -> {
                existing.setCode(academicYear.getCode());
                existing.setName(academicYear.getName());
                existing.setYear(academicYear.getYear());
                existing.setDescription(academicYear.getDescription());
                existing.setStartDate(academicYear.getStartDate());
                existing.setEndDate(academicYear.getEndDate());
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                return repository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("AcademicYear not found"));
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<AcademicYear> search(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }
}

