package com.example.demo.logs.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.logs.model.entity.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, UUID> {
    List<Log> findByUserId(UUID userId);
    // Matches entity field tableName (DB column: table_name)
    List<Log> findByTableName(String tableName);
    List<Log> findByAction(String action);
}
