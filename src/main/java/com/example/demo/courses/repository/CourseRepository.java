package com.example.demo.courses.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.courses.model.entity.Course;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    List<Course> findByNameContainingIgnoreCase(String name);
    List<Course> findByCodeContainingIgnoreCase(String code);
    List<Course> findByDepartmentId(UUID departmentId);
    boolean existsByCodeIgnoreCase(String code);
}

