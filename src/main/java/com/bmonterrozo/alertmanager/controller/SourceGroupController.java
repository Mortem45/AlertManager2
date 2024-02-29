package com.bmonterrozo.alertmanager.controller;

import com.bmonterrozo.alertmanager.entity.DataSource;
import com.bmonterrozo.alertmanager.entity.SourceGroup;
import com.bmonterrozo.alertmanager.service.DataSourceService;
import com.bmonterrozo.alertmanager.service.SourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sourceGroup")
public class SourceGroupController {
    @Autowired
    private SourceGroupService sourceGroupService;

    @Autowired
    private DataSourceService dataSourceService;

    @GetMapping
    public List<SourceGroup> findAll() {
        return sourceGroupService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<SourceGroup> findById(@PathVariable("id") int id) {
        return sourceGroupService.findById(id);
    }

    @PostMapping
    public SourceGroup save(@RequestBody SourceGroup sourceGroup) {
        return sourceGroupService.save(sourceGroup);
    }

    @PutMapping
    public SourceGroup update(@RequestBody SourceGroup sourceGroup) {
        return sourceGroupService.save(sourceGroup);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") int id) {
        return sourceGroupService.delete(id);
    }
//    TODO: ADD relations with Datasources

    @PutMapping("{sourceGroupId}/dataSource/{dataSourceId}")
    public SourceGroup addDataSource(
            @PathVariable("sourceGroupId") int groupId,
            @PathVariable("dataSourceId") int dataSourceId
    ) {
        SourceGroup sourceGroup = sourceGroupService.findById(groupId).get();
        DataSource dataSource = dataSourceService.findById(dataSourceId).get();
        sourceGroup.addDataSource(dataSource);
        return sourceGroupService.save(sourceGroup);
    }

    @DeleteMapping("{sourceGroupId}/dataSource/{dataSourceId}")
    public SourceGroup removeDataSource(
            @PathVariable("sourceGroupId") int sourceGroupId,
            @PathVariable("dataSourceId") int dataSourceId
    ) {
        SourceGroup sourceGroup = sourceGroupService.findById(sourceGroupId).get();
        sourceGroup.removeDataSource(dataSourceId);
        return sourceGroupService.save(sourceGroup);
    }
}
