package com.example.demo.departments.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.departments.model.entity.Department;
import com.example.demo.departments.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin
public class DepartmentApiController {

    private final DepartmentService service;

    public DepartmentApiController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Department> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Department getById(@PathVariable UUID id) { return service.getById(id).orElse(null); }

    @PostMapping
    public Department create(@RequestBody Department department) {
        return service.create(department);
    }

    @PutMapping("/{id}")
    public Department update(@PathVariable UUID id, @RequestBody Department department) {
        return service.update(id, department);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Department> search(@RequestParam String keyword) { return service.search(keyword); }
}
