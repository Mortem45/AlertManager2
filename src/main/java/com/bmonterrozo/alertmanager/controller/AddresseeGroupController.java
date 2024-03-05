package com.bmonterrozo.alertmanager.controller;

import com.bmonterrozo.alertmanager.entity.Addressee;
import com.bmonterrozo.alertmanager.entity.AddresseeGroup;
import com.bmonterrozo.alertmanager.service.AddresseeGroupService;
import com.bmonterrozo.alertmanager.service.AddresseeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/addresseeGroup")
public class AddresseeGroupController {
    @Autowired
    private AddresseeGroupService addresseeGroupService;

    @Autowired
    private AddresseeService  addresseeService;

    @GetMapping
    public List<AddresseeGroup> findAll() {
        return addresseeGroupService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<AddresseeGroup> findById(@PathVariable("id") int id) {
        return addresseeGroupService.findById(id);
    }

    @PostMapping
    public AddresseeGroup save(@RequestBody AddresseeGroup group) {
        return addresseeGroupService.save(group);
    }

    @PutMapping
    public AddresseeGroup update(@RequestBody AddresseeGroup addresseeGroup) {
        return addresseeGroupService.save(addresseeGroup);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return addresseeService.delete(id);
    }

    @PutMapping("{groupId}/contact/{contactId}")
    public AddresseeGroup addContact(
            @PathVariable("groupId") int groupId,
            @PathVariable("contactId") int contactId
    ) {
        AddresseeGroup addresseeGroup = addresseeGroupService.findById(groupId).get();
        Addressee addressee = addresseeService.findById(contactId).get();
        addresseeGroup.addAddressee(addressee);
        return addresseeGroupService.save(addresseeGroup);
    }

    @DeleteMapping("{addresseeGroupId}/addressee/{addresseeId}")
    public AddresseeGroup removeContact(
            @PathVariable("addresseeGroupId") int addresseeGroupId,
            @PathVariable("addresseeId") int addresseeId
    ) {
        AddresseeGroup addresseeGroup = addresseeGroupService.findById(addresseeGroupId).get();
        addresseeGroup.removeAddressee(addresseeId);
        return addresseeGroupService.save(addresseeGroup);
    }
}
