package com.example.demo.roles.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.logs.annotation.Loggable;

import com.example.demo.permissions.model.entity.Permission;
import com.example.demo.permissions.repository.PermissionRepository;
import com.example.demo.roles.model.entity.Role;
import com.example.demo.roles.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepo;

    private final PermissionRepository permRepo;

    public RoleService(RoleRepository roleRepo, PermissionRepository permRepo) {
        this.roleRepo = roleRepo;
        this.permRepo = permRepo;
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    public Page<Role> getRolesPaged(Pageable pageable) {
        return roleRepo.findAll(pageable);
    }

    public Role getRoleById(UUID id) {
        return roleRepo.findById(id).orElseThrow(() ->
            new RuntimeException("Role not found: " + id));
    }

    @Loggable(action = "CREATE", table = "roles")
    public Role createRole(Role role) {
        role.setCreatedAt(LocalDateTime.now());
        role.setIsActive(role.getIsActive() != null ? role.getIsActive() : true);
        return roleRepo.save(role);
    }

    @Loggable(action = "UPDATE", table = "roles")
    public Role updateRole(UUID id, Role updated) {
        Role existing = roleRepo.findById(id).orElseThrow(() ->
            new RuntimeException("Role not found: " + id));
        existing.setCode(updated.getCode());
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setIsActive(updated.getIsActive());
        existing.setIsSystem(updated.getIsSystem());
        existing.setUpdatedAt(LocalDateTime.now());
        return roleRepo.save(existing);
    }

    @Loggable(action = "DELETE", table = "roles")
    public void deleteRole(UUID id) {
        Role existing = roleRepo.findById(id).orElseThrow(() ->
            new RuntimeException("Role not found: " + id));
        existing.setDeletedAt(LocalDateTime.now());
        existing.setIsActive(false);
        roleRepo.save(existing);
    }

    public Role addPermission(UUID roleId, UUID permId) {
        Role r = roleRepo.findById(roleId).orElseThrow();
        Permission p = permRepo.findById(permId).orElseThrow();
        r.getPermissions().add(p);
        return roleRepo.save(r);
    }

    public Role removePermission(UUID roleId, UUID permId) {
        Role r = roleRepo.findById(roleId).orElseThrow();
        r.getPermissions().removeIf(p -> p.getId().equals(permId));
        return roleRepo.save(r);
    }
}