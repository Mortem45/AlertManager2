package com.bmonterrozo.alertmanager.controller;
import com.bmonterrozo.alertmanager.entity.Group;
import com.bmonterrozo.alertmanager.entity.Notification;
import com.bmonterrozo.alertmanager.service.GroupService;
import com.bmonterrozo.alertmanager.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private GroupService groupService;

    @GetMapping
    public List<Notification> findAll() {
        return notificationService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Notification> findById(@PathVariable("id") int id) {
        return notificationService.findById(id);
    }

    @PostMapping
    public Notification save(@RequestBody Notification notification) {
        return notificationService.save(notification);
    }

    @PutMapping
    public Notification update(@RequestBody Notification notification) {
        return notificationService.save(notification);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return notificationService.delete(id);
    }

    @PutMapping("{notificationId}/group/{groupId}")
    public Notification addDataSource(
            @PathVariable("notificationId") int notificationId,
            @PathVariable("groupId") int groupId
    ) {
        Notification notification = notificationService.findById(notificationId).get();
        Group group = groupService.findById(groupId).get();
        notification.addNotificationGroup(group);
        return notificationService.save(notification);
    }

    @DeleteMapping("{notificationId}/group/{groupId}")
    public Notification removeDataSource(
            @PathVariable("notificationId") int notificationId,
            @PathVariable("groupId") int groupId
    ) {
        Notification notification = notificationService.findById(notificationId).get();
        notification.removeNotificationGroup(groupId);
        return notificationService.save(notification);
    }
}
