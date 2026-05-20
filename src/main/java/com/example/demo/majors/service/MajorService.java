package com.example.demo.majors.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.logs.annotation.Loggable;

import com.example.demo.majors.model.entity.Major;
import com.example.demo.majors.repository.MajorRepository;

@Service
public class MajorService {
    private final MajorRepository repository;

    public MajorService(MajorRepository repository) {
        this.repository = repository;
    }

    public List<Major> getAll() {
        return repository.findAll();
    }

    public Optional<Major> getById(UUID id) {
        return repository.findById(id);
    }

    @Loggable(action = "CREATE", table = "majors")
    public Major create(Major major) {
        if (repository.existsByCodeIgnoreCase(major.getCode())) {
            throw new RuntimeException("Code already exists");
        }
        major.setCreatedAt(java.time.LocalDateTime.now());
        return repository.save(major);
    }

    @Loggable(action = "UPDATE", table = "majors")
    public Major update(UUID id, Major major) {
        return repository.findById(id)
            .map(existing -> {
                existing.setDepartmentId(major.getDepartmentId());
                existing.setCode(major.getCode());
                existing.setName(major.getName());
                existing.setDescription(major.getDescription());
                existing.setEffectiveDate(major.getEffectiveDate());
                existing.setExpiryDate(major.getExpiryDate());
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                return repository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Major not found"));
    }

    @Loggable(action = "DELETE", table = "majors")
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Major> search(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Major> getByDepartmentId(UUID departmentId) {
        return repository.findByDepartmentId(departmentId);
    }
}

