package com.bmonterrozo.alertmanager.repository;

import com.bmonterrozo.alertmanager.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Integer> {
}
