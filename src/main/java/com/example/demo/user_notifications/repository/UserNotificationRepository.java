package com.example.demo.user_notifications.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.user_notifications.model.entity.UserNotification;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UUID> {
    List<UserNotification> findByUserId(UUID userId);
    List<UserNotification> findByUserIdAndIsRead(UUID userId, Boolean isRead);
}
