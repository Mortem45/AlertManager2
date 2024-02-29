package com.bmonterrozo.alertmanager.controller;

import com.bmonterrozo.alertmanager.entity.DataSource;
import com.bmonterrozo.alertmanager.service.DataSourceService;
import com.bmonterrozo.alertmanager.service.SourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dataSource")
public class DataSourceController {
    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private SourceGroupService sourceGroupService;
    @GetMapping
    public List<DataSource> findAll() {
        return dataSourceService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<DataSource> findById(@PathVariable("id") int id) {
        return dataSourceService.findById(id);
    }

    @PostMapping
    public DataSource save(@RequestBody DataSource dataSource) {
        return dataSourceService.save(dataSource);
    }

    @PutMapping
    public DataSource update(@RequestBody DataSource dataSource) {
        return dataSourceService.save(dataSource);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return dataSourceService.delete(id);
    }
}
