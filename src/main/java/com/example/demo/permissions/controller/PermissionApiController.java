package com.example.demo.permissions.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.permissions.model.entity.Permission;
import com.example.demo.permissions.service.PermissionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/permissions")
@Tag(name = "Permissions", description = "API quản lý quyền hạn (Permissions)")
public class PermissionApiController {

    private final PermissionService service;

    public PermissionApiController(PermissionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả quyền hạn")
    public List<Permission> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy quyền hạn theo ID")
    public ResponseEntity<Permission> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Tạo quyền hạn mới")
    public ResponseEntity<Permission> create(@RequestBody Permission p) {
        return ResponseEntity.ok(service.create(p));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật quyền hạn")
    public ResponseEntity<Permission> update(@PathVariable UUID id, @RequestBody Permission p) {
        return ResponseEntity.ok(service.update(id, p));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá quyền hạn (soft delete)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}