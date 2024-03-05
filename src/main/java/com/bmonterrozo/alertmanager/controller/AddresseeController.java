package com.bmonterrozo.alertmanager.controller;
import com.bmonterrozo.alertmanager.entity.Addressee;
import com.bmonterrozo.alertmanager.service.AddresseeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/addressee")
public class AddresseeController {
    @Autowired
    private AddresseeService addresseeService;

    @GetMapping
    public List<Addressee> findAll() {
        return addresseeService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Addressee> findById(@PathVariable("id") int id) {
        return addresseeService.findById(id);
    }

    @PostMapping
    public Addressee save(@RequestBody Addressee contact) {
        return addresseeService.save(contact);
    }

    @PutMapping
    public Addressee update(@RequestBody Addressee addressee) {
        return addresseeService.save(addressee);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return addresseeService.delete(id);
    }
}
