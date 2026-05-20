package com.example.demo.positions.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.positions.model.entity.Position;
import com.example.demo.positions.repository.PositionRepository;

@Service
public class PositionService {
    private final PositionRepository repository;

    public PositionService(PositionRepository repository) {
        this.repository = repository;
    }

    public List<Position> getAll() {
        return repository.findAll();
    }

    public Optional<Position> getById(UUID id) {
        return repository.findById(id);
    }

    public Position create(Position position) {
        if (repository.existsByCodeIgnoreCase(position.getCode())) {
            throw new RuntimeException("Code already exists");
        }
        position.setCreatedAt(java.time.LocalDateTime.now());
        return repository.save(position);
    }

    public Position update(UUID id, Position position) {
        return repository.findById(id)
            .map(existing -> {
                existing.setCode(position.getCode());
                existing.setName(position.getName());
                existing.setDescription(position.getDescription());
                existing.setLevel(position.getLevel());
                existing.setDepartmentId(position.getDepartmentId());
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                return repository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Position not found"));
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Position> search(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Position> getByDepartment(UUID departmentId) {
        return repository.findByDepartmentId(departmentId);
    }
}

