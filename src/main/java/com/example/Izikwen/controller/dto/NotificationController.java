/*package com.example.Izikwen.controller.dto;

import com.example.Izikwen.notification.model.Notification;
import com.example.Izikwen.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public void send(@RequestParam Long userId,
                     @RequestParam String title,
                     @RequestParam String message) {

        service.send(userId, title, message);
    }

    @GetMapping("/{userId}")
    public List<Notification> get(@PathVariable Long userId) {
        return service.getUserNotifications(userId);
    }

    @PutMapping("/read/{id}")
    public void read(@PathVariable Long id) {
        service.markAsRead(id);
    }
}

*/