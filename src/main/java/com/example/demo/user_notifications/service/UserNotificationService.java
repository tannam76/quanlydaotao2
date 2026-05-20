package com.example.demo.user_notifications.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.user_notifications.model.entity.UserNotification;
import com.example.demo.user_notifications.repository.UserNotificationRepository;

@Service
public class UserNotificationService {

    private final UserNotificationRepository userNotificationRepository;

    public UserNotificationService(UserNotificationRepository userNotificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
    }

    public List<UserNotification> findAll() {
        return userNotificationRepository.findAll();
    }

    public Optional<UserNotification> findById(UUID id) {
        return userNotificationRepository.findById(id);
    }

    public List<UserNotification> findByUserId(UUID userId) {
        return userNotificationRepository.findByUserId(userId);
    }

    public List<UserNotification> findByUserIdAndIsRead(UUID userId, Boolean isRead) {
        return userNotificationRepository.findByUserIdAndIsRead(userId, isRead);
    }

    public UserNotification save(UserNotification userNotification) {
        return userNotificationRepository.save(userNotification);
    }

    public Optional<UserNotification> markAsRead(UUID id) {
        return userNotificationRepository.findById(id).map(un -> {
            un.setIsRead(true);
            un.setReadAt(LocalDateTime.now());
            return userNotificationRepository.save(un);
        });
    }

    public void deleteById(UUID id) {
        userNotificationRepository.deleteById(id);
    }
}
