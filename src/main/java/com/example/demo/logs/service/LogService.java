package com.example.demo.logs.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.logs.model.entity.Log;
import com.example.demo.logs.repository.LogRepository;

@Service
public class LogService {

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<Log> findAll() {
        return logRepository.findAll();
    }

    public Optional<Log> findById(UUID id) {
        return logRepository.findById(id);
    }

    public List<Log> findByUserId(UUID userId) {
        return logRepository.findByUserId(userId);
    }

    // tableName corresponds to DB column table_name
    public List<Log> findByTableName(String tableName) {
        return logRepository.findByTableName(tableName);
    }

    public List<Log> findByAction(String action) {
        return logRepository.findByAction(action);
    }

    public Log save(Log log) {
        return logRepository.save(log);
    }

    public void deleteById(UUID id) {
        logRepository.deleteById(id);
    }
}
