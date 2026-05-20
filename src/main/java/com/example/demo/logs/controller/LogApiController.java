package com.example.demo.logs.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.logs.model.entity.Log;
import com.example.demo.logs.service.LogService;

@RestController
@RequestMapping("/api/logs")
public class LogApiController {

    private final LogService logService;

    public LogApiController(LogService logService) {
        this.logService = logService;
    }

    // GET /api/logs
    // Optional query params: ?userId=...&tableName=...&action=...
    @GetMapping
    public ResponseEntity<List<Log>> getAll(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String tableName,
            @RequestParam(required = false) String action) {
        if (userId != null) {
            return ResponseEntity.ok(logService.findByUserId(userId));
        }
        if (tableName != null) {
            return ResponseEntity.ok(logService.findByTableName(tableName));
        }
        if (action != null) {
            return ResponseEntity.ok(logService.findByAction(action));
        }
        return ResponseEntity.ok(logService.findAll());
    }

    // GET /api/logs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Log> getById(@PathVariable UUID id) {
        return logService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/logs
    @PostMapping
    public ResponseEntity<Log> create(@RequestBody Log log) {
        log.setCreatedAt(LocalDateTime.now());
        log.setIsActive(true);
        Log saved = logService.save(log);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // DELETE /api/logs/{id}  (logs là dữ liệu audit — không có update)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return logService.findById(id).map(l -> {
            logService.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
