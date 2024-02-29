package com.bmonterrozo.alertmanager.service;

import com.bmonterrozo.alertmanager.entity.Group;
import com.bmonterrozo.alertmanager.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    public List<Group> findAll (){
        return groupRepository.findAll();
    }

    public Optional<Group> findById(int id) {
        return groupRepository.findById(id);
    }

    public Group save(Group group) {
        return groupRepository.save(group);
    }

    public boolean delete(int id) {
        return findById(id).map(group -> {
            groupRepository.delete(group);
            return true;
        }).orElse(false);
    }
}
