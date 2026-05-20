package com.example.demo.departments.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.logs.annotation.Loggable;

import com.example.demo.departments.model.entity.Department;
import com.example.demo.departments.repository.DepartmentRepository;

@Service
public class DepartmentService {
    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public List<Department> getAll() {
        return repository.findAll();
    }

    public Optional<Department> getById(UUID id) {
        return repository.findById(id);
    }

    @Loggable(action = "CREATE", table = "departments")
    public Department create(Department department) {
        if (repository.existsByCodeIgnoreCase(department.getCode())) {
            throw new RuntimeException("Code already exists");
        }
        department.setCreatedAt(java.time.LocalDateTime.now());
        return repository.save(department);
    }

    @Loggable(action = "UPDATE", table = "departments")
    public Department update(UUID id, Department department) {
        return repository.findById(id)
            .map(existing -> {
                existing.setCode(department.getCode());
                existing.setName(department.getName());
                existing.setDescription(department.getDescription());
                existing.setEstablishedDate(department.getEstablishedDate());
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                return repository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @Loggable(action = "DELETE", table = "departments")
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Department> search(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }
}

