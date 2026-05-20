package com.example.demo.logs.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.logs.model.entity.Log;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Tiện ích ghi nhật ký hoạt động hệ thống.
 * Inject class này vào bất kỳ Controller nào muốn ghi log.
 *
 * Ví dụ sử dụng:
 *   logHelper.log("CREATE", "students", saved.getId(), "Tạo sinh viên: " + saved.getFullname(), request);
 */
@Component
public class LogHelper {

    private final LogService logService;

    public LogHelper(LogService logService) {
        this.logService = logService;
    }

    /**
     * Ghi một bản ghi nhật ký vào database.
     *
     * @param action     Hành động: CREATE | UPDATE | DELETE | LOGIN | LOGOUT
     * @param tableName  Tên bảng dữ liệu liên quan (ví dụ: "students", "courses")
     * @param recordId   UUID của bản ghi bị tác động (có thể null)
     * @param description Mô tả chi tiết hành động
     * @param request    HttpServletRequest để lấy địa chỉ IP (có thể null)
     */
    public void log(String action,
                    String tableName,
                    UUID recordId,
                    String description,
                    HttpServletRequest request) {
        try {
            Log entry = new Log();
            entry.setAction(action);
            entry.setTableName(tableName);
            entry.setRecordId(recordId);
            entry.setDescription(description);
            entry.setCreatedAt(LocalDateTime.now());
            entry.setIsActive(true);
            if (request != null) {
                entry.setIpAddress(extractIp(request));
            }
            logService.save(entry);
        } catch (Exception e) {
            // Ghi log thất bại không được làm gián đoạn luồng nghiệp vụ chính
            System.err.println("[LogHelper] Không thể ghi log: " + e.getMessage());
        }
    }

    /** Lấy IP thực của client, hỗ trợ proxy / reverse-proxy */
    private String extractIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
            // X-Forwarded-For có thể chứa nhiều IP, lấy IP đầu tiên
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        return request.getRemoteAddr();
    }
}
