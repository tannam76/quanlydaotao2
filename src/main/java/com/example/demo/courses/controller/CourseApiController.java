package com.example.demo.courses.controller;

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

import com.example.demo.courses.model.entity.Course;
import com.example.demo.courses.service.CourseService;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin
public class CourseApiController {

    private final CourseService service;

    public CourseApiController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public List<Course> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Course getById(@PathVariable UUID id) { return service.getById(id).orElse(null); }

    @GetMapping("/department/{departmentId}")
    public List<Course> getByDepartment(@PathVariable UUID departmentId) {
        return service.getByDepartment(departmentId);
    }

    @PostMapping
    public Course create(@RequestBody Course course) {
        return service.create(course); // @Loggable trên CourseService tự ghi log
    }

    @PutMapping("/{id}")
    public Course update(@PathVariable UUID id, @RequestBody Course course) {
        return service.update(id, course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Course> search(@RequestParam String keyword) { return service.search(keyword); }
}
