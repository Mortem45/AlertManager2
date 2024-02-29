package com.bmonterrozo.alertmanager.service;

import com.bmonterrozo.alertmanager.entity.Platform;
import com.bmonterrozo.alertmanager.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatformService {
    @Autowired
    private PlatformRepository platformRepository;

    public List<Platform> findAll (){
        return platformRepository.findAll();
    }

    public Optional<Platform> findById(int id) {
        return platformRepository.findById(id);
    }

    public Platform save(Platform platform) {
        return platformRepository.save(platform);
    }

    public boolean delete(int id) {
        return findById(id).map(platform -> {
            platformRepository.delete(platform);
            return true;
        }).orElse(false);
    }
}
