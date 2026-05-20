package com.example.demo.semesters.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.semesters.model.entity.Semester;
import com.example.demo.semesters.repository.SemesterRepository;

@Service
public class SemesterService {
    private final SemesterRepository repository;

    public SemesterService(SemesterRepository repository) {
        this.repository = repository;
    }

    public List<Semester> getAll() {
        return repository.findAll();
    }

    public List<Semester> getActive() {
        return repository.findByIsActiveTrue();
    }

    public List<Semester> getBySchoolYear(UUID schoolYearId) {
        return repository.findBySchoolYearId(schoolYearId);
    }

    public Optional<Semester> getById(UUID id) {
        return repository.findById(id);
    }

    public Semester create(Semester semester) {
        semester.setCreatedAt(java.time.LocalDateTime.now());
        return repository.save(semester);
    }

    public Semester update(UUID id, Semester semester) {
        return repository.findById(id)
            .map(existing -> {
                existing.setCode(semester.getCode());
                existing.setName(semester.getName());
                existing.setSchoolYearId(semester.getSchoolYearId());
                existing.setSchoolYearName(semester.getSchoolYearName());
                existing.setStartDate(semester.getStartDate());
                existing.setEndDate(semester.getEndDate());
                existing.setUpdatedAt(java.time.LocalDateTime.now());
                return repository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Semester not found"));
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Semester> search(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }
}

