package com.example.demo.auth.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.LoginResponse;
import com.example.demo.users.model.entity.User;
import com.example.demo.users.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Đăng nhập / Đăng xuất hệ thống")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập hệ thống")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
                                               HttpSession session) {
        if (request.getUsername() == null || request.getUsername().isBlank() ||
            request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse("Tên đăng nhập và mật khẩu không được để trống"));
        }

        try {
            // Tìm user theo username (dùng findFirstByUsername để tránh load list dư)
            Optional<User> userOpt = userRepository.findFirstByUsername(request.getUsername().trim());

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401)
                        .body(new LoginResponse("Tên đăng nhập hoặc mật khẩu không đúng"));
            }

            User user = userOpt.get();

            // Kiểm tra tài khoản có bị xóa/vô hiệu hóa không
            if (user.getDeletedAt() != null) {
                return ResponseEntity.status(403)
                        .body(new LoginResponse("Tài khoản đã bị xóa khỏi hệ thống"));
            }
            if (Boolean.FALSE.equals(user.getIsActive())) {
                return ResponseEntity.status(403)
                        .body(new LoginResponse("Tài khoản đã bị vô hiệu hóa"));
            }

            // So sánh mật khẩu (plain text)
            String storedPwd = user.getPassword() == null ? "" : user.getPassword().trim();
            if (!request.getPassword().equals(storedPwd)) {
                return ResponseEntity.status(401)
                        .body(new LoginResponse("Tên đăng nhập hoặc mật khẩu không đúng"));
            }

            // Cập nhật last_login_at
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);

            // Lấy vai trò đầu tiên
            String role = user.getRoles().stream()
                    .findFirst()
                    .map(r -> r.getName())
                    .orElse("USER");

            // Lưu thông tin vào session
            session.setAttribute("currentUserId", user.getId());
            session.setAttribute("currentUsername", user.getUsername());
            session.setAttribute("currentUserRole", role);

            LoginResponse resp = new LoginResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getAvatarUrl(),
                    role
            );

            return ResponseEntity.ok(resp);

        } catch (Exception ex) {
            ex.printStackTrace();  // In ra console/log để debug
            return ResponseEntity.status(500)
                    .body(new LoginResponse("Lỗi hệ thống: " + ex.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất hệ thống")
    public ResponseEntity<LoginResponse> logout(HttpSession session) {
        session.invalidate();
        LoginResponse resp = new LoginResponse();
        resp.setSuccess(true);
        resp.setMessage("Đăng xuất thành công");
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    @Operation(summary = "Lấy thông tin người dùng đang đăng nhập")
    public ResponseEntity<LoginResponse> me(HttpSession session) {
        Object userId = session.getAttribute("currentUserId");
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse("Chưa đăng nhập"));
        }

        String username = (String) session.getAttribute("currentUsername");
        String role = (String) session.getAttribute("currentUserRole");

        LoginResponse resp = new LoginResponse();
        resp.setSuccess(true);
        resp.setMessage("OK");
        resp.setUsername(username);
        resp.setRole(role);
        return ResponseEntity.ok(resp);
    }
}
