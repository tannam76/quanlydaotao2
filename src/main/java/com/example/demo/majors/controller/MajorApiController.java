package com.example.demo.majors.controller;

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

import com.example.demo.majors.model.entity.Major;
import com.example.demo.majors.service.MajorService;

@RestController
@RequestMapping("/api/majors")
@CrossOrigin
public class MajorApiController {

    private final MajorService service;

    public MajorApiController(MajorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Major> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Major getById(@PathVariable UUID id) { return service.getById(id).orElse(null); }

    @PostMapping
    public Major create(@RequestBody Major major) {
        return service.create(major);
    }

    @PutMapping("/{id}")
    public Major update(@PathVariable UUID id, @RequestBody Major major) {
        return service.update(id, major);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Major> search(@RequestParam String keyword) { return service.search(keyword); }

    @GetMapping("/department/{departmentId}")
    public List<Major> getByDepartment(@PathVariable UUID departmentId) {
        return service.getByDepartmentId(departmentId);
    }
}
