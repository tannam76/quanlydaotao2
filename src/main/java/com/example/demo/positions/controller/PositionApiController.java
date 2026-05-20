package com.example.demo.positions.controller;

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

import com.example.demo.positions.model.entity.Position;
import com.example.demo.positions.service.PositionService;

@RestController
@RequestMapping("/api/positions")
@CrossOrigin
public class PositionApiController {

    private final PositionService service;

    public PositionApiController(PositionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Position> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Position getById(@PathVariable UUID id) {
        return service.getById(id).orElse(null);
    }

    @GetMapping("/department/{departmentId}")
    public List<Position> getByDepartment(@PathVariable UUID departmentId) {
        return service.getByDepartment(departmentId);
    }

    @PostMapping
    public Position create(@RequestBody Position position) {
        return service.create(position);
    }

    @PutMapping("/{id}")
    public Position update(@PathVariable UUID id, @RequestBody Position position) {
        return service.update(id, position);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public List<Position> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}

