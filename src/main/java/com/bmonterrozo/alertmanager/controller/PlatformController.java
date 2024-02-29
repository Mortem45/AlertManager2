package com.bmonterrozo.alertmanager.controller;

import com.bmonterrozo.alertmanager.entity.Platform;
import com.bmonterrozo.alertmanager.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/platform")
public class PlatformController {
    @Autowired
    private PlatformService platformService;

    @GetMapping
    public List<Platform> findAll() {
        return platformService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Platform> findById(@PathVariable("id") int id) {
        return platformService.findById(id);
    }

    @PostMapping
    public Platform save(@RequestBody Platform platform) {
        return platformService.save(platform);
    }

    @PutMapping
    public Platform update(@RequestBody Platform platform) {
        return platformService.save(platform);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return platformService.delete(id);
    }
}
