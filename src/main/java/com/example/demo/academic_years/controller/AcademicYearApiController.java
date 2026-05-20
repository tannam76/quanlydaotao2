package com.example.demo.academic_years.controller;

import java.util.List;
import java.util.UUID;

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

import com.example.demo.academic_years.model.entity.AcademicYear;
import com.example.demo.academic_years.service.AcademicYearService;

@RestController
@RequestMapping("/api/academic-years")
@CrossOrigin
public class AcademicYearApiController {

    private final AcademicYearService service;

    public AcademicYearApiController(AcademicYearService service) {
        this.service = service;
    }

    @GetMapping
    public List<AcademicYear> getAll() {
        return service.getAll();
    }

    @GetMapping("/active")
    public List<AcademicYear> getActive() {
        return service.getActive();
    }

    @GetMapping("/{id}")
    public AcademicYear getById(@PathVariable UUID id) {
        return service.getById(id).orElse(null);
    }

    @PostMapping
    public AcademicYear create(@RequestBody AcademicYear academicYear) {
        return service.create(academicYear);
    }

    @PutMapping("/{id}")
    public AcademicYear update(@PathVariable UUID id, @RequestBody AcademicYear academicYear) {
        return service.update(id, academicYear);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public List<AcademicYear> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}

