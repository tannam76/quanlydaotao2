package com.example.demo.settings.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.settings.model.entity.Setting;
import com.example.demo.settings.repository.SettingRepository;

@Service
public class SettingService {

    private final SettingRepository settingRepository;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public List<Setting> findAll() {
        return settingRepository.findAll();
    }

    public Optional<Setting> findById(UUID id) {
        return settingRepository.findById(id);
    }

    public Optional<Setting> findByKey(String key) {
        return settingRepository.findByKey(key);
    }

    public Setting save(Setting setting) {
        return settingRepository.save(setting);
    }

    public void deleteById(UUID id) {
        settingRepository.deleteById(id);
    }
}
