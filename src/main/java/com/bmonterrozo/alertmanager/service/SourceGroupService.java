package com.bmonterrozo.alertmanager.service;

import com.bmonterrozo.alertmanager.entity.SourceGroup;
import com.bmonterrozo.alertmanager.repository.SourceGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SourceGroupService {
    @Autowired
    private SourceGroupRepository sourceGroupRepository;

    public List<SourceGroup> findAll (){
        return sourceGroupRepository.findAll();
    }

    public Optional<SourceGroup> findById(int id) {
        return sourceGroupRepository.findById(id);
    }

    public SourceGroup save(SourceGroup sourceGroup) {
        return sourceGroupRepository.save(sourceGroup);
    }

    public boolean delete(int id) {
        return findById(id).map(sourceGroup -> {
            sourceGroupRepository.delete(sourceGroup);
            return true;
        }).orElse(false);
    }
}
