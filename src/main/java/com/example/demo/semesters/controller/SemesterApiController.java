package com.example.demo.semesters.controller;

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

import com.example.demo.semesters.model.entity.Semester;
import com.example.demo.semesters.service.SemesterService;

@RestController
@RequestMapping("/api/semesters")
@CrossOrigin
public class SemesterApiController {

    private final SemesterService service;

    public SemesterApiController(SemesterService service) {
        this.service = service;
    }

    @GetMapping
    public List<Semester> getAll() {
        return service.getAll();
    }

    @GetMapping("/active")
    public List<Semester> getActive() {
        return service.getActive();
    }

    @GetMapping("/school-year/{schoolYearId}")
    public List<Semester> getBySchoolYear(@PathVariable UUID schoolYearId) {
        return service.getBySchoolYear(schoolYearId);
    }

    @GetMapping("/{id}")
    public Semester getById(@PathVariable UUID id) {
        return service.getById(id).orElse(null);
    }

    @PostMapping
    public Semester create(@RequestBody Semester semester) {
        return service.create(semester);
    }

    @PutMapping("/{id}")
    public Semester update(@PathVariable UUID id, @RequestBody Semester semester) {
        return service.update(id, semester);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public List<Semester> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}

