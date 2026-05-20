package com.example.demo.auth.dto;

import java.util.UUID;

public class LoginResponse {
    private boolean success;
    private String message;
    private UUID userId;
    private String username;
    private String email;
    private String avatarUrl;
    private String role;       // vai trò đầu tiên (nếu có)

    public LoginResponse() {}

    // Constructor thành công
    public LoginResponse(UUID userId, String username, String email, String avatarUrl, String role) {
        this.success = true;
        this.message = "Đăng nhập thành công";
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }

    // Constructor thất bại
    public LoginResponse(String errorMessage) {
        this.success = false;
        this.message = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
