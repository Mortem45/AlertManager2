package com.bmonterrozo.alertmanager.controller;

import com.bmonterrozo.alertmanager.entity.DataSourceType;
import com.bmonterrozo.alertmanager.service.DataSourceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dataSourceType")
public class DataSourceTypeController {
    @Autowired
    private DataSourceTypeService dataSourceTypeService;

    @GetMapping
    public List<DataSourceType> findAll() {
        return dataSourceTypeService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<DataSourceType> findById(@PathVariable("id") int id) {
        return dataSourceTypeService.findById(id);
    }

    @PostMapping
    public DataSourceType save(@RequestBody DataSourceType dataSourceType) {
        return dataSourceTypeService.save(dataSourceType);
    }

    @PutMapping
    public DataSourceType update(@RequestBody DataSourceType dataSourceType) {
        return dataSourceTypeService.save(dataSourceType);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return dataSourceTypeService.delete(id);
    }
}
