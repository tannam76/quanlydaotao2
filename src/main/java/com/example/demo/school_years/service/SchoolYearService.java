package com.example.demo.school_years.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.school_years.model.entity.SchoolYear;
import com.example.demo.school_years.repository.SchoolYearRepository;

@Service
public class SchoolYearService {
    private final SchoolYearRepository repository;

    public SchoolYearService(SchoolYearRepository repository) {
        this.repository = repository;
    }

    public List<SchoolYear> getAll() {
        return repository.findAll();
    }

    public List<SchoolYear> getActive() {
        return repository.findByIsActiveTrue();
    }

    public Optional<SchoolYear> getById(UUID id) {
        return repository.findById(id);
    }

    public SchoolYear create(SchoolYear schoolYear) {
        schoolYear.setCreatedAt(java.time.LocalDateTime.now());
        return repository.save(schoolYear);
    }

    public SchoolYear update(UUID id, SchoolYear schoolYear) {
        return repository.findById(id)
            .map(existing -> {
                existing.setCode(schoolYear.getCode());
                existing.setName(schoolYear.getName());
                existing.setDescription(schoolYear.getDescription());
                existing.setNote(schoolYear.getNote());
                existing.setStartDate(schoolYear.getStartDate());
                existing.setEndDate(schoolYear.getEndDate());
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                return repository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("SchoolYear not found"));
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<SchoolYear> search(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }
}

