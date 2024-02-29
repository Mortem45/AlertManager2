package com.bmonterrozo.alertmanager.controller;

import com.bmonterrozo.alertmanager.entity.Contact;
import com.bmonterrozo.alertmanager.entity.Group;
import com.bmonterrozo.alertmanager.service.ContactService;
import com.bmonterrozo.alertmanager.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private ContactService contactService;

    @GetMapping
    public List<Group> findAll() {
        return groupService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Group> findById(@PathVariable("id") int id) {
        return groupService.findById(id);
    }

    @PostMapping
    public Group save(@RequestBody Group group) {
        return groupService.save(group);
    }

    @PutMapping
    public Group update(@RequestBody Group group) {
        return groupService.save(group);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return groupService.delete(id);
    }

    @PutMapping("{groupId}/contact/{contactId}")
    public Group addContact(
            @PathVariable("groupId") int groupId,
            @PathVariable("contactId") int contactId
    ) {
        Group group = groupService.findById(groupId).get();
        Contact contact = contactService.findById(contactId).get();
        group.addContact(contact);
        return groupService.save(group);
    }

    @DeleteMapping("{groupId}/contact/{contactId}")
    public Group removeContact(
            @PathVariable("groupId") int groupId,
            @PathVariable("contactId") int contactId
    ) {
        Group group = groupService.findById(groupId).get();
        group.removeContact(contactId);
        return groupService.save(group);
    }
}
