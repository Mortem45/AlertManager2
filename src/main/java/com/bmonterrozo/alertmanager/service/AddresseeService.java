package com.bmonterrozo.alertmanager.service;

import com.bmonterrozo.alertmanager.entity.Addressee;
import com.bmonterrozo.alertmanager.repository.AddresseeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddresseeService {
    @Autowired
    private AddresseeRepository addresseeRepository;

    public List<Addressee> findAll (){
        return addresseeRepository.findAll();
    }

    public Optional<Addressee> findById(int id) {
        return addresseeRepository.findById(id);
    }

    public Addressee save(Addressee addressee) {
        return addresseeRepository.save(addressee);
    }

    public boolean delete(int id) {
        return findById(id).map(addressee -> {
            addresseeRepository.delete(addressee);
            return true;
        }).orElse(false);
    }
}
