package com.example.demo.users.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.roles.model.entity.Role;
import com.example.demo.roles.repository.RoleRepository;
import com.example.demo.users.model.entity.User;
import com.example.demo.users.service.UserService;
import com.example.demo.users.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API quản lý người dùng")
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public UserApiController(UserService userService, UserRepository userRepo, RoleRepository roleRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả người dùng")
    public List<User> list() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy người dùng theo ID")
    public ResponseEntity<User> getById(@PathVariable UUID id) {
        return userRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Tạo người dùng mới")
    public ResponseEntity<User> create(@RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(user.getIsActive() != null ? user.getIsActive() : true);
        return ResponseEntity.ok(userRepo.save(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật người dùng")
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User updated) {
        return userRepo.findById(id).map(existing -> {
            existing.setUsername(updated.getUsername());
            existing.setEmail(updated.getEmail());
            existing.setPhone(updated.getPhone());
            existing.setAvatarUrl(updated.getAvatarUrl());
            existing.setIsActive(updated.getIsActive());
            if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
                existing.setPassword(updated.getPassword());
            }
            existing.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(userRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá người dùng (soft delete)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return userRepo.findById(id).map(u -> {
            u.setDeletedAt(LocalDateTime.now());
            u.setIsActive(false);
            userRepo.save(u);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ===== Quản lý Vai trò của User =====

    @GetMapping("/{userId}/roles")
    @Operation(summary = "Lấy danh sách vai trò của người dùng")
    public ResponseEntity<?> getUserRoles(@PathVariable UUID userId) {
        return userRepo.findById(userId)
                .map(u -> ResponseEntity.ok(u.getRoles()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @Operation(summary = "Gán vai trò cho người dùng")
    public ResponseEntity<User> assignRole(@PathVariable UUID userId, @PathVariable UUID roleId) {
        User u = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role r = roleRepo.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        u.getRoles().add(r);
        return ResponseEntity.ok(userRepo.save(u));
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @Operation(summary = "Gỡ vai trò khỏi người dùng")
    public ResponseEntity<User> removeRole(@PathVariable UUID userId, @PathVariable UUID roleId) {
        User u = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        u.getRoles().removeIf(r -> r.getId().equals(roleId));
        return ResponseEntity.ok(userRepo.save(u));
    }
}