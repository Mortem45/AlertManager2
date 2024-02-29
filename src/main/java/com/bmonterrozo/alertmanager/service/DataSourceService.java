package com.bmonterrozo.alertmanager.service;

import com.bmonterrozo.alertmanager.entity.DataSource;
import com.bmonterrozo.alertmanager.repository.DataSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataSourceService {
    @Autowired
    private DataSourceRepository dataSourceRepository;

    public List<DataSource> findAll (){
        return dataSourceRepository.findAll();
    }

    public Optional<DataSource> findById(int id) {
        return dataSourceRepository.findById(id);
    }

    public DataSource save(DataSource dataSource) {
        return dataSourceRepository.save(dataSource);
    }

    public boolean delete(int id) {
        return findById(id).map(dataSource -> {
            dataSourceRepository.delete(dataSource);
            return true;
        }).orElse(false);
    }
}
