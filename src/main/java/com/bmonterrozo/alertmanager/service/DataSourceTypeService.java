package com.bmonterrozo.alertmanager.service;

import com.bmonterrozo.alertmanager.entity.DataSourceType;
import com.bmonterrozo.alertmanager.repository.DataSourceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataSourceTypeService {
    @Autowired
    private DataSourceTypeRepository dataSourceTypeRepository;

    public List<DataSourceType> findAll (){
        return dataSourceTypeRepository.findAll();
    }

    public Optional<DataSourceType> findById(int id) {
        return dataSourceTypeRepository.findById(id);
    }

    public DataSourceType save(DataSourceType dataSource) {
        return dataSourceTypeRepository.save(dataSource);
    }

    public boolean delete(int id) {
        return findById(id).map(dataSource -> {
            dataSourceTypeRepository.delete(dataSource);
            return true;
        }).orElse(false);
    }
}
