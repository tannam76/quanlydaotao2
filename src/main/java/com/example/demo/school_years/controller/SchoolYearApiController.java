package com.example.demo.school_years.controller;

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

import com.example.demo.school_years.model.entity.SchoolYear;
import com.example.demo.school_years.service.SchoolYearService;

@RestController
@RequestMapping("/api/school-years")
@CrossOrigin
public class SchoolYearApiController {

    private final SchoolYearService service;

    public SchoolYearApiController(SchoolYearService service) {
        this.service = service;
    }

    @GetMapping
    public List<SchoolYear> getAll() {
        return service.getAll();
    }

    @GetMapping("/active")
    public List<SchoolYear> getActive() {
        return service.getActive();
    }

    @GetMapping("/{id}")
    public SchoolYear getById(@PathVariable UUID id) {
        return service.getById(id).orElse(null);
    }

    @PostMapping
    public SchoolYear create(@RequestBody SchoolYear schoolYear) {
        return service.create(schoolYear);
    }

    @PutMapping("/{id}")
    public SchoolYear update(@PathVariable UUID id, @RequestBody SchoolYear schoolYear) {
        return service.update(id, schoolYear);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public List<SchoolYear> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}

