package com.example.Izikwen.controller.dto;

import com.example.Izikwen.notification.model.Notification;
import com.example.Izikwen.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService service;


    @PostMapping("/send")
    public void send(@RequestParam Long userId,
                     @RequestParam String title,
                     @RequestParam String message) {

        log.info("Sending notification to user {}", userId);
        service.send(userId, title, message);
    }

    // get user notifications
    @GetMapping("/{userId}")
    public List<Notification> get(@PathVariable Long userId) {
        return service.getUserNotifications(userId);
    }

    // mark as read
    @PutMapping("/read/{id}")
    public void read(@PathVariable Long id) {
        service.markAsRead(id);
    }
}
