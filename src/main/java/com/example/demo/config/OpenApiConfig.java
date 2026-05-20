package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Quản Lý Đào Tạo – API Documentation")
                        .description("Hệ thống REST API cho ứng dụng Quản Lý Đào Tạo. " +
                                "Cung cấp các endpoint để quản lý: " +
                                "Vai trò & Phân quyền (Roles & Permissions), " +
                                "Người dùng (Users), Thông báo (Notifications), " +
                                "Khoá học, Học kỳ, Năm học...")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Dev Team")
                                .email("admin@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server")
                ));
    }
}
