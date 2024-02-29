package com.bmonterrozo.alertmanager.service;

import com.bmonterrozo.alertmanager.entity.Contact;
import com.bmonterrozo.alertmanager.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> findAll (){
        return contactRepository.findAll();
    }

    public Optional<Contact> findById(int id) {
        return contactRepository.findById(id);
    }

    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    public boolean delete(int id) {
        return findById(id).map(contact -> {
            contactRepository.delete(contact);
            return true;
        }).orElse(false);
    }
}
