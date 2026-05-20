package com.example.demo.logs.aspect;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.logs.annotation.Loggable;
import com.example.demo.logs.model.entity.Log;
import com.example.demo.logs.service.LogService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * AOP Aspect tự động ghi nhật ký khi bất kỳ method nào
 * được đánh dấu @Loggable hoàn thành thành công.
 *
 * - Chạy AFTER method thành công (không ghi log nếu method ném exception)
 * - Lấy IP từ RequestContextHolder (hoạt động trong web request context)
 * - Tự động lấy recordId từ return value nếu object có getId()
 * - Nếu ghi log thất bại → không ảnh hưởng luồng nghiệp vụ chính
 */
@Aspect
@Component
public class ActivityLogAspect {

    private final LogService logService;

    public ActivityLogAspect(LogService logService) {
        this.logService = logService;
    }

    /**
     * Intercept mọi method có annotation @Loggable
     */
    @Around("@annotation(com.example.demo.logs.annotation.Loggable)")
    public Object logActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        // Thực thi method gốc trước
        Object result = joinPoint.proceed();

        // Sau khi method hoàn thành thành công → ghi log
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Loggable loggable = method.getAnnotation(Loggable.class);

            String action    = loggable.action();
            String tableName = loggable.table();
            String desc      = buildDescription(loggable, action, tableName, result, joinPoint);
            UUID   recordId  = extractRecordId(result);
            String ip        = extractIp();

            Log entry = new Log();
            entry.setAction(action);
            entry.setTableName(tableName);
            entry.setRecordId(recordId);
            entry.setDescription(desc);
            entry.setIpAddress(ip);
            entry.setCreatedAt(LocalDateTime.now());
            entry.setIsActive(true);

            logService.save(entry);

        } catch (Exception ex) {
            // Lỗi ghi log KHÔNG được lan ra ngoài
            System.err.println("[ActivityLogAspect] Không thể ghi log: " + ex.getMessage());
        }

        return result;
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /**
     * Xây dựng mô tả log.
     * Nếu annotation có description → dùng nguyên (thay {table}, {action}).
     * Nếu không → tự động tạo từ action + table + tên method.
     */
    private String buildDescription(Loggable loggable, String action, String tableName,
                                    Object result, ProceedingJoinPoint joinPoint) {
        String custom = loggable.description();
        if (custom != null && !custom.isBlank()) {
            return custom.replace("{table}", tableName).replace("{action}", action);
        }

        // Tự tạo description
        String methodName = joinPoint.getSignature().getName();
        String label = switch (action.toUpperCase()) {
            case "CREATE" -> "Tạo mới bản ghi trong";
            case "UPDATE" -> "Cập nhật bản ghi trong";
            case "DELETE" -> "Xóa bản ghi khỏi";
            case "LOGIN"  -> "Đăng nhập vào";
            case "LOGOUT" -> "Đăng xuất khỏi";
            default       -> "Thực thi [" + action + "] trên";
        };

        // Thử lấy tên hoặc ID từ return value
        String detail = "";
        if (result != null) {
            // Thử gọi getName() / getFullname() / getFullName() / getCode() / getUsername()
            for (String getter : new String[]{"getName","getFullname","getFullName","getCode","getUsername","getTitle"}) {
                try {
                    Method m = result.getClass().getMethod(getter);
                    Object val = m.invoke(result);
                    if (val != null) {
                        detail = ": " + val;
                        break;
                    }
                } catch (NoSuchMethodException ignored) {
                    // object không có getter này → thử getter khác
                } catch (Exception ignored) {}
            }

            // Nếu không tìm được tên → thử lấy ID
            if (detail.isEmpty()) {
                UUID id = extractRecordId(result);
                if (id != null) detail = " [ID: " + id + "]";
            }
        }

        // DELETE không có return object → lấy từ argument
        if (action.equalsIgnoreCase("DELETE") && detail.isEmpty()) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] != null) {
                detail = " [ID: " + args[0] + "]";
            }
        }

        return label + " bảng [" + tableName + "]" + detail;
    }

    /**
     * Cố lấy UUID id từ return value bằng reflection.
     * Hỗ trợ các object có phương thức getId() trả về UUID.
     */
    private UUID extractRecordId(Object result) {
        if (result == null) return null;
        try {
            Method getId = result.getClass().getMethod("getId");
            Object val = getId.invoke(result);
            if (val instanceof UUID) return (UUID) val;
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * Lấy IP từ HttpServletRequest hiện tại (qua RequestContextHolder).
     * Hỗ trợ proxy / reverse-proxy qua các header chuẩn.
     * Trả về null nếu không có request context (batch job, v.v.)
     */
    private String extractIp() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return null;

            HttpServletRequest request = attrs.getRequest();

            // Thứ tự ưu tiên: X-Forwarded-For → X-Real-IP → RemoteAddr
            String ip = request.getHeader("X-Forwarded-For");
            if (isValidIp(ip)) return ip.split(",")[0].trim();

            ip = request.getHeader("X-Real-IP");
            if (isValidIp(ip)) return ip.trim();

            return request.getRemoteAddr();
        } catch (Exception ignored) {
            return null;
        }
    }

    private boolean isValidIp(String ip) {
        return ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip);
    }
}
