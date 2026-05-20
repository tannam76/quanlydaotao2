package com.example.demo.positions.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.positions.model.entity.Position;

public interface PositionRepository extends JpaRepository<Position, UUID> {
    List<Position> findByNameContainingIgnoreCase(String name);
    List<Position> findByCodeContainingIgnoreCase(String code);
    List<Position> findByDepartmentId(UUID departmentId);
    boolean existsByCodeIgnoreCase(String code);
}

