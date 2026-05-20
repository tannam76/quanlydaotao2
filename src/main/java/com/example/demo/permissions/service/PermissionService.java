package com.example.demo.permissions.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.permissions.model.entity.Permission;
import com.example.demo.permissions.repository.PermissionRepository;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepo;

    public PermissionService(PermissionRepository permissionRepo) {
        this.permissionRepo = permissionRepo;
    }

    public Permission create(Permission permission) {
        permission.setIsActive(permission.getIsActive() != null ? permission.getIsActive() : true);
        return permissionRepo.save(permission);
    }

    public List<Permission> findAll() {
        return permissionRepo.findAll();
    }

    public Permission findById(UUID id) {
        return permissionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + id));
    }

    public Permission update(UUID id, Permission updated) {
        Permission existing = findById(id);
        existing.setCode(updated.getCode());
        existing.setName(updated.getName());
        existing.setModule(updated.getModule());
        existing.setAction(updated.getAction());
        existing.setDescription(updated.getDescription());
        existing.setIsActive(updated.getIsActive());
        existing.setUpdatedAt(LocalDateTime.now());
        return permissionRepo.save(existing);
    }

    public void delete(UUID id) {
        Permission existing = findById(id);
        existing.setDeletedAt(LocalDateTime.now());
        existing.setIsActive(false);
        permissionRepo.save(existing);
    }
}