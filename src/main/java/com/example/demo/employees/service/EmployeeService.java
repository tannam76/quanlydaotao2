package com.example.demo.employees.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.logs.annotation.Loggable;

import com.example.demo.employees.model.entity.Employee;
import com.example.demo.employees.repository.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Optional<Employee> getById(UUID id) {
        return repository.findById(id);
    }

    @Loggable(action = "CREATE", table = "employees")
    public Employee create(Employee employee) {
        if (repository.existsByCodeIgnoreCase(employee.getCode())) {
            throw new RuntimeException("Code already exists");
        }
        employee.setCreatedAt(java.time.LocalDateTime.now());
        return repository.save(employee);
    }

    @Loggable(action = "UPDATE", table = "employees")
    public Employee update(UUID id, Employee employee) {
        return repository.findById(id)
            .map(existing -> {
                existing.setCode(employee.getCode());
                existing.setFullName(employee.getFullName());
                existing.setEmail(employee.getEmail());
                existing.setPhone(employee.getPhone());
                existing.setDepartmentId(employee.getDepartmentId());
                existing.setPositionId(employee.getPositionId());
                existing.setHireDate(employee.getHireDate());
                existing.setSalaryCoefficient(employee.getSalaryCoefficient());
                existing.setAcademicDegree(employee.getAcademicDegree());
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                return repository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Loggable(action = "DELETE", table = "employees")
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Employee> search(String keyword) {
        return repository.findByFullNameContainingIgnoreCase(keyword);
    }

    public List<Employee> getByDepartment(UUID departmentId) {
        return repository.findByDepartmentId(departmentId);
    }
}

