package com.bmonterrozo.alertmanager.service;

import com.bmonterrozo.alertmanager.entity.Alert;
import com.bmonterrozo.alertmanager.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;

    public List<Alert> findAll() {
        return (List<Alert>) alertRepository.findAll();
    }

    public Optional<Alert> findById(int id) {
        return alertRepository.findById(id);
    }

    public Alert save(Alert alert) {
        return alertRepository.save(alert);
    }

    public boolean delete(int alertId) {
        return findById(alertId).map(alert -> {
            alertRepository.delete(alert);
            return true;
        }).orElse(false);
    }
}
