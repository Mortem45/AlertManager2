package com.bmonterrozo.alertmanager.controller;
import com.bmonterrozo.alertmanager.entity.Contact;
import com.bmonterrozo.alertmanager.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping
    public List<Contact> findAll() {
        return contactService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Contact> findById(@PathVariable("id") int id) {
        return contactService.findById(id);
    }

    @PostMapping
    public Contact save(@RequestBody Contact contact) {
        return contactService.save(contact);
    }

    @PutMapping
    public Contact update(@RequestBody Contact contact) {
        return contactService.save(contact);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return contactService.delete(id);
    }
}
