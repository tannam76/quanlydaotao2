package com.example.demo.settings.controller;

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

import com.example.demo.settings.model.entity.Setting;
import com.example.demo.settings.service.SettingService;

@RestController
@RequestMapping("/api/settings")
public class SettingApiController {

    private final SettingService settingService;

    public SettingApiController(SettingService settingService) {
        this.settingService = settingService;
    }

    // GET /api/settings
    // Optional query param: ?key=...
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String key) {
        if (key != null) {
            return settingService.findByKey(key)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        List<Setting> settings = settingService.findAll();
        return ResponseEntity.ok(settings);
    }

    // GET /api/settings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Setting> getById(@PathVariable UUID id) {
        return settingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/settings
    @PostMapping
    public ResponseEntity<Setting> create(@RequestBody Setting setting) {
        setting.setCreatedAt(LocalDateTime.now());
        setting.setIsActive(true);
        Setting saved = settingService.save(setting);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/settings/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Setting> update(@PathVariable UUID id,
                                          @RequestBody Setting updated) {
        return settingService.findById(id).map(existing -> {
            existing.setKey(updated.getKey());
            existing.setValue(updated.getValue());
            existing.setDescription(updated.getDescription());
            existing.setIsActive(updated.getIsActive());
            existing.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(settingService.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/settings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return settingService.findById(id).map(s -> {
            settingService.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
