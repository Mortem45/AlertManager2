package com.bmonterrozo.alertmanager.repository;

import com.bmonterrozo.alertmanager.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {
}
