package com.bmonterrozo.alertmanager.controller;
import com.bmonterrozo.alertmanager.entity.AddresseeGroup;
import com.bmonterrozo.alertmanager.entity.Notification;
import com.bmonterrozo.alertmanager.service.AddresseeGroupService;
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
    private AddresseeGroupService addresseeGroupService;

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

    @PutMapping("{notificationId}/addresseeGroup/{addresseeGroupId}")
    public Notification addAddresseeGroupNotification(
            @PathVariable("notificationId") int notificationId,
            @PathVariable("addresseeGroupId") int addresseeGroupId
    ) {
        Notification notification = notificationService.findById(notificationId).get();
        AddresseeGroup addresseeGroup = addresseeGroupService.findById(addresseeGroupId).get();
        notification.addNotificationAddresseeGroup(addresseeGroup);
        return notificationService.save(notification);
    }

    @DeleteMapping("{notificationId}/addresseeGroup/{addresseeGroupId}")
    public Notification removeAddresseeGroupNotification(
            @PathVariable("notificationId") int notificationId,
            @PathVariable("addresseeGroupId") int addresseeGroupId
    ) {
        Notification notification = notificationService.findById(notificationId).get();
        notification.removeNotificationAddresseeGroup(addresseeGroupId);
        return notificationService.save(notification);
    }
}
