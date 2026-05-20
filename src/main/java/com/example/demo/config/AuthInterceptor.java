package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor kiểm tra session đăng nhập cho tất cả các API endpoint.
 * Các path công khai (login, swagger, static) sẽ được bỏ qua.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Cho phép OPTIONS (CORS preflight) qua
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // Whitelist: các path không cần xác thực
        if (isPublicPath(path)) {
            return true;
        }

        // Kiểm tra session
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUserId") != null) {
            return true;
        }

        // Chưa đăng nhập → trả về 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"success\":false,\"message\":\"Chưa đăng nhập hoặc phiên làm việc đã hết hạn\"}");
        return false;
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/api/auth")          // Đăng nhập / đăng xuất
            || path.startsWith("/swagger-ui")        // Swagger UI
            || path.startsWith("/api-docs")          // OpenAPI docs
            || path.startsWith("/v3/api-docs")
            || path.startsWith("/webjars")
            || path.startsWith("/login")
            || path.equals("/")
            || path.endsWith(".html")
            || path.endsWith(".css")
            || path.endsWith(".js")
            || path.endsWith(".png")
            || path.endsWith(".jpg")
            || path.endsWith(".ico")
            || path.endsWith(".json");
    }
}
