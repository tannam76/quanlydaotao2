package com.example.demo.user_notifications.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user_notifications.model.entity.UserNotification;
import com.example.demo.user_notifications.service.UserNotificationService;

@RestController
@RequestMapping("/api/user-notifications")
public class UserNotificationApiController {

    private final UserNotificationService userNotificationService;

    public UserNotificationApiController(UserNotificationService userNotificationService) {
        this.userNotificationService = userNotificationService;
    }

    // GET /api/user-notifications
    @GetMapping
    public ResponseEntity<List<UserNotification>> getAll() {
        return ResponseEntity.ok(userNotificationService.findAll());
    }

    // GET /api/user-notifications/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserNotification> getById(@PathVariable UUID id) {
        return userNotificationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/user-notifications/user/{userId}
    // Optional query param: ?isRead=true|false
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserNotification>> getByUserId(
            @PathVariable UUID userId,
            @RequestParam(required = false) Boolean isRead) {
        if (isRead != null) {
            return ResponseEntity.ok(userNotificationService.findByUserIdAndIsRead(userId, isRead));
        }
        return ResponseEntity.ok(userNotificationService.findByUserId(userId));
    }

    // POST /api/user-notifications
    @PostMapping
    public ResponseEntity<UserNotification> create(@RequestBody UserNotification userNotification) {
        userNotification.setCreatedAt(LocalDateTime.now());
        userNotification.setIsRead(false);
        userNotification.setIsActive(true);
        UserNotification saved = userNotificationService.save(userNotification);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/user-notifications/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserNotification> update(@PathVariable UUID id,
                                                   @RequestBody UserNotification updated) {
        return userNotificationService.findById(id).map(existing -> {
            existing.setUserId(updated.getUserId());
            existing.setNotificationId(updated.getNotificationId());
            existing.setIsRead(updated.getIsRead());
            existing.setReadAt(updated.getReadAt());
            existing.setIsActive(updated.getIsActive());
            return ResponseEntity.ok(userNotificationService.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/user-notifications/{id}/read
    @PutMapping("/{id}/read")
    public ResponseEntity<UserNotification> markAsRead(@PathVariable UUID id) {
        return userNotificationService.markAsRead(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/user-notifications/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return userNotificationService.findById(id).map(un -> {
            userNotificationService.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
