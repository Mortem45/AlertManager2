package com.bmonterrozo.alertmanager.repository;

import com.bmonterrozo.alertmanager.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Integer> {
}
