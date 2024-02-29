package com.bmonterrozo.alertmanager.service;

import com.bmonterrozo.alertmanager.entity.Notification;
import com.bmonterrozo.alertmanager.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> findAll (){
        return notificationRepository.findAll();
    }

    public Optional<Notification> findById(int id) {
        return notificationRepository.findById(id);
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public boolean delete(int id) {
        return findById(id).map(notification -> {
            notificationRepository.delete(notification);
            return true;
        }).orElse(false);
    }
}
