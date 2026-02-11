package com.example.Izikwen.service;


import com.example.Izikwen.notification.model.Notification;
import com.example.Izikwen.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public void send(Long userId, String title, String message) {
        Notification notification = new Notification(userId, title, message);
        repository.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification n = repository.findById(notificationId)
                .orElseThrow();
        n.setRead(true);
        repository.save(n);
    }
}