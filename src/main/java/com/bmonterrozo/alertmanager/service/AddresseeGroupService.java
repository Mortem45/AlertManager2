package com.bmonterrozo.alertmanager.service;

import com.bmonterrozo.alertmanager.entity.AddresseeGroup;
import com.bmonterrozo.alertmanager.repository.AddresseeGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddresseeGroupService {
    @Autowired
    private AddresseeGroupRepository addresseeGroupRepository;

    public List<AddresseeGroup> findAll (){
        return addresseeGroupRepository.findAll();
    }

    public Optional<AddresseeGroup> findById(int id) {
        return addresseeGroupRepository.findById(id);
    }

    public AddresseeGroup save(AddresseeGroup group) {
        return addresseeGroupRepository.save(group);
    }

    public boolean delete(int id) {
        return findById(id).map(addresseeGroup -> {
            addresseeGroupRepository.delete(addresseeGroup);
            return true;
        }).orElse(false);
    }
}
