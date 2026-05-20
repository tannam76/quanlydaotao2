package com.example.demo.notifications.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.notifications.model.entity.Notification;
import com.example.demo.notifications.service.NotificationService;

@Tag(name = "Notifications", description = "API quản lý thông báo (Notifications)")
@RestController
@RequestMapping("/api/notifications")
public class NotificationApiController {

    private final NotificationService notificationService;

    public NotificationApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // GET /api/notifications
    @Operation(summary = "Lấy tất cả thông báo", description = "Trả về danh sách toàn bộ thông báo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Thành công")
    })
    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    // GET /api/notifications/{id}
    @Operation(summary = "Lấy thông báo theo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tìm thấy thông báo"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy thông báo")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(
            @Parameter(description = "UUID của thông báo") @PathVariable UUID id) {
        return notificationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/notifications
    @Operation(summary = "Tạo thông báo mới")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tạo thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    })
    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody Notification notification) {
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsActive(true);
        Notification saved = notificationService.save(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/notifications/{id}
    @Operation(summary = "Cập nhật thông báo theo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy thông báo")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(
            @Parameter(description = "UUID của thông báo") @PathVariable UUID id,
            @RequestBody Notification updated) {
        return notificationService.findById(id).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setContent(updated.getContent());
            existing.setTypeId(updated.getTypeId());
            existing.setTargetRoleId(updated.getTargetRoleId());
            existing.setIsActive(updated.getIsActive());
            existing.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(notificationService.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/notifications/{id}
    @Operation(summary = "Xoá thông báo theo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Xoá thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy thông báo")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "UUID của thông báo") @PathVariable UUID id) {
        return notificationService.findById(id).map(n -> {
            notificationService.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
