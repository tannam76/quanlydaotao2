package com.example.demo.courses.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.logs.annotation.Loggable;

import com.example.demo.courses.model.entity.Course;
import com.example.demo.courses.repository.CourseRepository;

@Service
public class CourseService {
    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public List<Course> getAll() {
        return repository.findAll();
    }

    public Optional<Course> getById(UUID id) {
        return repository.findById(id);
    }

    @Loggable(action = "CREATE", table = "courses")
    public Course create(Course course) {
        if (repository.existsByCodeIgnoreCase(course.getCode())) {
            throw new RuntimeException("Code already exists");
        }
        course.setCreatedAt(java.time.LocalDateTime.now());
        return repository.save(course);
    }

    @Loggable(action = "UPDATE", table = "courses")
    public Course update(UUID id, Course course) {
        return repository.findById(id)
            .map(existing -> {
                existing.setDepartmentId(course.getDepartmentId());
                existing.setCode(course.getCode());
                existing.setName(course.getName());
                existing.setNameEn(course.getNameEn());
                existing.setCredits(course.getCredits());
                existing.setCourseType(course.getCourseType());
                existing.setTheoryHours(course.getTheoryHours());
                existing.setPracticeHours(course.getPracticeHours());
                existing.setSelfStudyHours(course.getSelfStudyHours());
                existing.setInternshipCredits(course.getInternshipCredits());
                existing.setDescription(course.getDescription());
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                return repository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @Loggable(action = "DELETE", table = "courses")
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Course> search(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Course> getByDepartment(UUID departmentId) {
        return repository.findByDepartmentId(departmentId);
    }
}

