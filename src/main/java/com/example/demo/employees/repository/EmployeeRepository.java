package com.example.demo.employees.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.employees.model.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByFullNameContainingIgnoreCase(String fullName);
    List<Employee> findByCodeContainingIgnoreCase(String code);
    List<Employee> findByDepartmentId(UUID departmentId);
    boolean existsByCodeIgnoreCase(String code);
}

