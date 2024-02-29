package com.bmonterrozo.alertmanager.controller;

import com.bmonterrozo.alertmanager.entity.Alert;
import com.bmonterrozo.alertmanager.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alert")
public class AlertController {
    @Autowired
    private AlertService alertService;

    @GetMapping
    public List<Alert> findAll() {
        return alertService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Alert> findById(@PathVariable("id") int id) {
        return alertService.findById(id);
    }

    @PostMapping
    public Alert save(@RequestBody Alert alert) {
        return alertService.save(alert);
    }

    @PutMapping
    public Alert update(@RequestBody Alert alert) {
        return alertService.save(alert);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return alertService.delete(id);
    }
}
