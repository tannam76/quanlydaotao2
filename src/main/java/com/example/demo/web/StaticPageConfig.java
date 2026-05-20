package com.example.demo.web;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Đã chuyển sang com.example.demo.config.WebConfig – giữ lại class này để không lỗi compile
// @Configuration  <-- đã tắt, WebConfig mới đảm nhiệm
public class StaticPageConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Handled by WebConfig
    }
}