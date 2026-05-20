package com.example.demo.logs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Đánh dấu một method Service cần được ghi nhật ký tự động.
 *
 * Cách dùng:
 * <pre>
 *   {@literal @}Loggable(action = "CREATE", table = "students")
 *   public Student create(Student student) { ... }
 * </pre>
 *
 * AOP Aspect sẽ tự động:
 * - Ghi action, table vào bảng logs
 * - Lấy record_id từ return value (nếu có getId())
 * - Lấy IP từ HttpServletRequest hiện tại
 * - Tạo description tự động
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {

    /** Hành động: CREATE | UPDATE | DELETE | LOGIN | LOGOUT */
    String action();

    /** Tên bảng DB liên quan, ví dụ: "students", "courses" */
    String table();

    /**
     * Mô tả tùy chỉnh. Nếu để trống, Aspect sẽ tự tạo description.
     * Hỗ trợ placeholder: {table}, {action}
     */
    String description() default "";
}
