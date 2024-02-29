package com.bmonterrozo.alertmanager.repository;

import com.bmonterrozo.alertmanager.entity.DataSourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceTypeRepository extends JpaRepository<DataSourceType, Integer> {
}
